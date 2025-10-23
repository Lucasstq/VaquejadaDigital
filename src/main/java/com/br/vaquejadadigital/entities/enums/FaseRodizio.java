package com.br.vaquejadadigital.entities.enums;

public enum FaseRodizio {
    CLASSIFICATORIA("Classificatória"),
    DISPUTA("Disputa"),
    RETORNO("Retorno"),
    RABO_DA_GATA("Rabo da Gata");

    private final String descricao;

    FaseRodizio(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
