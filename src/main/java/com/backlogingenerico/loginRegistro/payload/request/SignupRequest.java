package com.backlogingenerico.loginRegistro.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Data
public class SignupRequest {
    @NotNull(message = "O nome é obrigatório.\n")
    private String username;

    @NotNull(message = "O CPF é Obrigatório.\n")
    private String cpf;

    @NotNull(message = "O telefone é obrigatório.\n")
    private String telefone;

    private boolean isWhatsapp = true;

    @NotNull(message = "Só é possível criar uma conta após aceitar os termos de uso.\n")
    public boolean isAceitouTermos() {
        return aceitouTermos;
    }

    public void setAceitouTermos(boolean aceitouTermos) {
        this.aceitouTermos = aceitouTermos;
    }

    private boolean aceitouTermos = false;
    @NotBlank(message = "O email é obrigatório.\n")
    @Email
    private String email;

    private Set<String> role;

    @NotBlank(message = "A senha é obrigatória.\n")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}
