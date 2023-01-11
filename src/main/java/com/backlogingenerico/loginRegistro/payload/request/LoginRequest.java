package com.backlogingenerico.loginRegistro.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email inválido")
    private String email;
    @NotBlank(message = "Senha inválido")
    private String password;


}
