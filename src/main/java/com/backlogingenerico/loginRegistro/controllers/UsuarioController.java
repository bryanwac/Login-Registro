package com.backlogingenerico.loginRegistro.controllers;

import com.backlogingenerico.loginRegistro.DTO.AlterarDadosDTO;
import com.backlogingenerico.loginRegistro.exception.ApiException;
import com.backlogingenerico.loginRegistro.models.Usuario;
import com.backlogingenerico.loginRegistro.repositories.UsuarioRepository;
import com.backlogingenerico.loginRegistro.services.UserService;
import com.backlogingenerico.loginRegistro.util.DecodedToken;
import com.backlogingenerico.loginRegistro.util.EmailValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {


    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    UserService usuarioService;


    @GetMapping()
    public ResponseEntity<?> getUserBoard() {
        try {
            return ResponseEntity.ok("Acesso liberado a página inicial de Usuario.");
        } catch (Exception e) {
            throw new ApiException("Acesso negado a área de Usuario, você não possui permissão. Faça login primeiro.", e);
        }
    }


    @PostMapping("/perfil/alterar-dados")
    public ResponseEntity<?> alteraraDados(@RequestHeader String Authorization, @RequestBody AlterarDadosDTO dto){
        try {
            DecodedToken tokenDec = DecodedToken.getDecoded(Authorization);
            Usuario user = usuarioRepository.findUserByEmail(tokenDec.sub).orElseThrow();

            if (usuarioRepository.existsByEmail(dto.getEmail())){
                throw new ApiException("Este não é um email válido.");
            } else if (usuarioRepository.existsByEmail(user.getEmail())){
                if (user.getUsername() != null && user.getUsername() == dto.getNome()){
                    throw new ApiException("O novo Nome de Usuário é igual antigo.");
                } else if (dto.getEmail() != null && user.getEmail() == dto.getEmail()){
                    throw new ApiException("O Email inserido é idêntico ao antigo.");
                } else if (dto.getEmail() != null && !EmailValidate.isValid(dto.getEmail())) {
                    throw new ApiException("O Email inserido não é válido. Verifique e tente novamente.");
                } else if (user.getTelefone() != null && user.getTelefone() == dto.getTelefone()) {
                    throw new ApiException("O telefone inserido é idêntio ao antigo.");
                } else {
                    return ResponseEntity.ok(usuarioService.alterarDados(user, dto));
                }
            } else {
                throw new ApiException("Ocorreu um erro inesperado. Entre em contato com o administrador do sistema.");
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
