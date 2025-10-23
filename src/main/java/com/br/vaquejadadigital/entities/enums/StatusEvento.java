package com.br.vaquejadadigital.entities.enums;

public enum StatusEvento {
    CRIADO("Criado"),
    EM_ANDAMENTO("Em Andamento"),
    FINALIZADO("Finalizado"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusEvento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
