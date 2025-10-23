package com.br.vaquejadadigital.entities.enums;

public enum TipoNotificacao {
    CHAMADA_PISTA("Chamada para a Pista"),
    ALTERACAO_ORDEM("Alteração na Ordem"),
    RETORNO("Direito a Retorno"),
    RABO_DA_GATA("Incluído no Rabo da Gata"),
    RESULTADO_REGISTRADO("Resultado Registrado"),
    EVENTO_INICIADO("Evento Iniciado"),
    RODIZIO_INICIADO("Rodízio Iniciado");

    private final String descricao;

    TipoNotificacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
