package com.br.vaquejadadigital.entities.enums;

public enum TipoResultado {
    VALEU_O_BOI("Valeu o Boi"),
    ZERO("Zero"),
    RETORNO("Retorno"),
    FALTA("Falta"),
    DESCLASSIFICADO("Desclassificado");

    private final String descricao;

    TipoResultado(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
