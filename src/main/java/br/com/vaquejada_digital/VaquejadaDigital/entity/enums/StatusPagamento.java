package br.com.vaquejada_digital.VaquejadaDigital.entity.enums;

public enum StatusPagamento {

    PENDENTE("Aguardando Pagamento"),
    EM_ANALISE("Pagamento em Análise"),
    PAGO("Pago"),
    CANCELADO("Cancelado"),
    EXPIRADO("Expirado"),
    REEMBOLSADO("Reembolsado"),
    PARCIALMENTE_PAGO("Parcialmente Pago");

    private final String descricao;

    StatusPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
