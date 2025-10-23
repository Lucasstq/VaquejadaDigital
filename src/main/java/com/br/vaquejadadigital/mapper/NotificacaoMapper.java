package com.br.vaquejadadigital.mapper;

import com.br.vaquejadadigital.dtos.response.NotificacaoResponse;
import com.br.vaquejadadigital.entities.Notificacao;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NotificacaoMapper {

    public NotificacaoResponse toResponse(Notificacao notificacao) {
        return NotificacaoResponse.builder()
                .id(notificacao.getId())
                .tipo(notificacao.getTipo())
                .mensagem(notificacao.getMensagem())
                .lida(notificacao.getLida())
                .eventoId(notificacao.getEventoId())
                .rodizioId(notificacao.getRodizioId())
                .ordemCorridaId(notificacao.getOrdemCorridaId())
                .dataCriacao(notificacao.getDataCriacao())
                .dataLeitura(notificacao.getDataLeitura())
                .build();
    }
}
