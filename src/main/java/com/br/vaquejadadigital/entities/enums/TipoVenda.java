package com.br.vaquejadadigital.entities.enums;

public enum TipoVenda {
    ONLINE("Online"),
    MANUAL("Manual");

    private final String descricao;

    TipoVenda(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
