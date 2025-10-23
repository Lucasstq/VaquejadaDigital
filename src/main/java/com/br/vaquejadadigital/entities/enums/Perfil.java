package com.br.vaquejadadigital.entities.enums;

public enum Perfil {
    ADMIN("Administrador"),
    JUIZ("Juiz"),
    LOCUTOR("Locutor"),
    CORREDOR("Corredor");

    private final String descricao;

    Perfil(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
