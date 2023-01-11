package com.backlogingenerico.loginRegistro.DTO;

import com.backlogingenerico.loginRegistro.models.Permissao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioDto {

    @ReadOnlyProperty
    private Long id;
    private String nome;
    private String email;
    private Set<Permissao> permissoes;


}
