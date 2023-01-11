package com.backlogingenerico.loginRegistro.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "permissoes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Permissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PermissaoId")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EPermissao nome;
}