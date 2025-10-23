package com.br.vaquejadadigital.entities.enums;

public enum TipoCategoria {
    ASPIRANTE("Aspirante"),
    AMADOR("Amador"),
    PROFISSIONAL("Profissional"),
    FEMININO("Feminino");

    private final String descricao;

    TipoCategoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
