package com.backlogingenerico.loginRegistro.controllers;

import com.backlogingenerico.loginRegistro.DTO.TokenDTO;
import com.backlogingenerico.loginRegistro.exception.ApiException;
import com.backlogingenerico.loginRegistro.models.EPermissao;
import com.backlogingenerico.loginRegistro.models.Permissao;
import com.backlogingenerico.loginRegistro.models.Usuario;
import com.backlogingenerico.loginRegistro.payload.request.LoginRequest;
import com.backlogingenerico.loginRegistro.payload.request.SignupRequest;
import com.backlogingenerico.loginRegistro.payload.response.MessageResponse;
import com.backlogingenerico.loginRegistro.repositories.PermissaoRepository;
import com.backlogingenerico.loginRegistro.repositories.UsuarioRepository;
import com.backlogingenerico.loginRegistro.services.TokenService;
import com.backlogingenerico.loginRegistro.services.UserService;
import com.backlogingenerico.loginRegistro.util.EmailValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class OpenController {

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    UserService usuarioService;

    @Autowired
    PermissaoRepository permissaoRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    TokenService tokenService;

    @PostMapping("/registro")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signupRequest) {
        if (usuarioRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erro: Email já está em uso."));
        }
        if (usuarioRepository.existsByCpf(signupRequest.getCpf())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erro: CPF já está em uso."));
        }
        if (!EmailValidate.isValid(signupRequest.getEmail())){
            throw new ApiException("O email inserido não é valido, verifique e tente novamente.");
        }
        if (!signupRequest.isAceitouTermos()) {
            throw new ApiException("Só é possível se cadastrar após concordar com os termos de uso");
        }

        //Criando nova conta de user
        Usuario usuario = new Usuario(
                             signupRequest.getUsername(),
                             signupRequest.getCpf(),
                             signupRequest.getTelefone(),
                             signupRequest.getEmail(),
                             encoder.encode(signupRequest.getPassword()),
                             signupRequest.isAceitouTermos());

        Set<String> strRoles = signupRequest.getRole();
        Set<Permissao> roles = new HashSet<>();

        if (strRoles == null) {
            Permissao userRole = permissaoRepository.findByNome(EPermissao.PERM_USER)
                    .orElseThrow(() -> new RuntimeException("Erro: Permissão não encontrada."));
            roles.add(userRole);
        } else { // se a role pré-setada no register for admin, adiciona a ROLE_ADMIN pro user
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    Permissao adminRole = permissaoRepository.findByNome(EPermissao.PERM_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Erro: Permissão não encontrada"));
                    roles.add(adminRole);
                } else {
                    Permissao userRole = permissaoRepository.findByNome(EPermissao.PERM_USER)
                            .orElseThrow(() -> new RuntimeException("Erro: Permissão não encontrada"));
                    roles.add(userRole);
                }
            });
        }
        usuario.setPermissoes(roles);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new MessageResponse("Usuario registrado com sucesso!"));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            String token = tokenService.generateToken(authentication, loginRequest);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok(TokenDTO.builder().type("Bearer ").token(token).build());
        } catch (AuthenticationException e) {
            throw new ApiException("Usuario ou Senha inválidos.");
        }
    }

    @GetMapping("/retornauserportoken")
    public ResponseEntity<?> buscarRolesPorToken() {
        return ResponseEntity.ok(usuarioService.findUserInfoByToken());
    }


}
