package com.backlogingenerico.loginRegistro.repositories;

import com.backlogingenerico.loginRegistro.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findUserByUsername(String username);

    Optional<Usuario> findUserByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByCpf(String cpf);

    Usuario findByCpf (String cpf);


}
