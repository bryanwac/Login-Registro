package com.backlogingenerico.loginRegistro.models;


import javax.persistence.*;

@Entity
@Table(name = "permissoes")
public class Permissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PermissaoId")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EPermissao nome;

    public Permissao() {

    }

    public Permissao(EPermissao name) {
        this.nome = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EPermissao getNome() {
        return nome;
    }

    public void setNome(EPermissao nome) {
        this.nome = nome;
    }
}