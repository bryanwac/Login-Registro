package com.backlogingenerico.loginRegistro.repositories;

import com.backlogingenerico.loginRegistro.models.EPermissao;
import com.backlogingenerico.loginRegistro.models.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

    Optional<Permissao> findByNome(EPermissao name);
}
