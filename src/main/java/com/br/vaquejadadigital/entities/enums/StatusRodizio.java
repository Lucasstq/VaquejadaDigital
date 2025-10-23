package com.br.vaquejadadigital.entities.enums;

public enum StatusRodizio {
    AGUARDANDO("Aguardando"),
    EM_ANDAMENTO("Em Andamento"),
    FINALIZADO("Finalizado");

    private final String descricao;

    StatusRodizio(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
