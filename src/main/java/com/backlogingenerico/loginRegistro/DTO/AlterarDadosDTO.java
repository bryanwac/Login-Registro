package com.backlogingenerico.loginRegistro.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlterarDadosDTO {

    private String nome;
    private String email;
    private String telefone;
}
