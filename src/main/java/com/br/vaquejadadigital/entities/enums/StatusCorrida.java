package com.br.vaquejadadigital.entities.enums;

public enum StatusCorrida {
    PRONTO("Pronto"),
    CHAMADO("Chamado"),
    CORRENDO("Correndo"),
    CORREU("Correu"),
    FALTA("Falta");

    private final String descricao;

    StatusCorrida(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
