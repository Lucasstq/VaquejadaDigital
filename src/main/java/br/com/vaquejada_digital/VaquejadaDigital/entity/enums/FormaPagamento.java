package br.com.vaquejada_digital.VaquejadaDigital.entity.enums;

public enum FormaPagamento {

    ONLINE("Pagamento Online (Gateway)"),
    PIX("PIX"),
    DINHEIRO("Dinheiro"),
    CARTAO_CREDITO("Cartão de Crédito"),
    CARTAO_DEBITO("Cartão de Débito"),
    TRANSFERENCIA("Transferência Bancária"),
    CHEQUE("Cheque");

    private final String descricao;

    FormaPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}