package com.backlogingenerico.loginRegistro.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email inválido")
    private String email;
    @NotBlank(message = "Senha inválido")
    private String password;


}
