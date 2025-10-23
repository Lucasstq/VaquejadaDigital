package com.br.vaquejadadigital.entities.enums;

public enum FormaPagamento {
    PIX("PIX"),
    CARTAO_CREDITO("Cartão de Crédito"),
    CARTAO_DEBITO("Cartão de Débito"),
    DINHEIRO("Dinheiro"),
    PAGO_NA_ARENA("Pago na Arena");

    private final String descricao;

    FormaPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}