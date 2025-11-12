package com.br.vaquejadadigital.mapper;

import com.br.vaquejadadigital.dtos.request.NotificacaoRequest;
import com.br.vaquejadadigital.dtos.response.NotificacaoResponse;
import com.br.vaquejadadigital.entities.Notificacao;
import com.br.vaquejadadigital.entities.Usuario;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NotificacaoMapper {

    public static Notificacao toEntity(NotificacaoRequest request, Usuario usuario) {
        return Notificacao.builder()
                .usuario(usuario)
                .mensagem(request.mensagem())
                .tipo(request.tipo())
                .lida(false)
                .eventoId(request.eventoId())
                .rodizioId(request.rodizioId())
                .ordemCorridaId(request.ordemCorridaId())
                .build();
    }


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
