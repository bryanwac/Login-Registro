package com.backlogingenerico.loginRegistro.services;

import com.backlogingenerico.loginRegistro.DTO.AlterarDadosDTO;
import com.backlogingenerico.loginRegistro.DTO.UsuarioDto;
import com.backlogingenerico.loginRegistro.exception.ApiException;
import com.backlogingenerico.loginRegistro.models.Usuario;
import com.backlogingenerico.loginRegistro.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> acharUsuarioPorEmail(String email) {
        return usuarioRepository.findUserByEmail(email);
    }

    public Optional<Usuario> findUserById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario getUserByEmail(String email) {
        return usuarioRepository.findUserByEmail(email).orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado pelo e-mail."));
    }

    public Usuario getUserByCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf);
    }

    public Usuario getUserByUsername(String username) {
        return usuarioRepository.findUserByUsername(username).orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado pelo nome."));
    }

    public Usuario getUserById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado."));
    }

    public boolean userExists(Usuario usuario) {
        var usuarioTemp = usuarioRepository.findUserByEmail(usuario.getEmail());
        if (usuarioTemp.isPresent()) return true;
        usuarioTemp = usuarioRepository.findUserByUsername(usuario.getUsername());
        return usuarioTemp.isPresent();
    }

    public UsuarioDto findUserInfoByToken() {
        try {
            Usuario usuario = usuarioRepository.findUserByEmail((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado."));

            UsuarioDto dto = new UsuarioDto();
            dto.setId(usuario.getId());
            dto.setPermissoes(usuario.getPermissoes());
            dto.setNome(usuario.getUsername());
            dto.setEmail(usuario.getEmail());

            return dto;
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new ApiException("Aconteceu um erro ao buscar o usuario.");
        }
    }

    public Usuario setNovaSenha(String cpf, String senha) {
        try {
            Usuario usuario;
            if (cpf != null) {
                usuario = usuarioRepository.findByCpf(cpf);
                usuario.setSenhaAntiga(usuario.getPassword());
                usuario.setPassword(senha);
                usuario.setSenhaFoiAlterada(true);
                usuario.setDataAlteracaoSenha(LocalDateTime.now());
            } else {
                throw new ApiException("Usuario não encontrado.");
            }

            usuarioRepository.save(usuario);

            return usuario;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AlterarDadosDTO alterarDados(Usuario entity, AlterarDadosDTO dto) {

        if (dto.getNome() != null || dto.getEmail() != null || dto.getTelefone() != null) {
            Usuario user = acharUsuarioPorEmail(entity.getEmail()).orElseThrow();

            if (dto.getNome() != null) {
                user.setUsername(dto.getNome());
            } else if (dto.getEmail() != null) {
                user.setEmail(dto.getEmail());
            } else if (dto.getTelefone() != null) {
                user.setTelefone(dto.getTelefone());
            }
            usuarioRepository.save(user);
        }
        return dto;
    }
}
