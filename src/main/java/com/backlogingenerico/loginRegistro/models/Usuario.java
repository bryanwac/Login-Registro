package com.backlogingenerico.loginRegistro.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Usuario",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "UsuarioEmail"),
                @UniqueConstraint(columnNames = "UsuarioCPF")
        })
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UsuarioId")
    private Long id;

    @NotBlank
    @Column(name = "UsuarioNome")
    private String username; // Nome completo do user

    @NotBlank
    @Basic(optional = false)
    @Column(name = "UsuarioCPF", nullable = false)
    private String cpf;

    @PrePersist
    void beforeInsert() {
        if (cpf !=null)
            this.cpf = cpf.trim().replace(".", "").replace("-", "");
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_permissoes",
            joinColumns = @JoinColumn(name = "UsuarioId"),
            inverseJoinColumns = @JoinColumn(name = "PermissaoId"))
    private Set<Permissao> permissoes = new HashSet<>();

    @NotBlank
    @Basic(optional = false)
    @Column(name = "UsuarioTelefone", nullable = false)
    private String telefone;

    @Basic()
    @Column(name = "UsuarioTeleWhatsapp")
    private boolean eWhatsapp = false;

    @Column(name = "UsuarioAceitouTermos")
    private boolean aceitouTermos = false;

    @NotBlank
    @Email
    @Basic(optional = false)
    @Column(name = "UsuarioEmail", nullable = false)
    private String email;

    @NotBlank
    @Basic(optional = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "UsuarioSenha", nullable = false)
    private String password;

    @Column(name = "UsuarioSenhaFoiAlterada")
    private boolean senhaFoiAlterada = false;

    @Column(name = "UsuarioDataAlteracaoSenha")
    private LocalDateTime dataAlteracaoSenha = null;

    @Column(name = "UsuarioSenhaAntiga")
    private String senhaAntiga;
    @Column
    private boolean enabled = true;

    public Usuario(String username, String cpf, String telefone, String email, String password, Boolean aceitouTermos) {
        this.username = username;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.password = password;
        this.aceitouTermos = aceitouTermos;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (var r : this.permissoes) {
            var sga = new SimpleGrantedAuthority(r.getNome().toString());
            authorities.add(sga);
        }
        return authorities;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
