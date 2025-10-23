package com.br.vaquejadadigital.entities.enums;

public enum StatusSenha {
    DISPONIVEL("Disponível"),
    RESERVADA("Reservada"),
    VENDIDA("Vendida"),
    BLOQUEADA("Bloqueada");

    private final String descricao;

    StatusSenha(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

