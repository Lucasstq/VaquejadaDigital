package com.br.vaquejadadigital.dtos.response;

import com.br.vaquejadadigital.entities.enums.TipoNotificacao;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificacaoResponse(
        Long id,
        TipoNotificacao tipo,
        String mensagem,
        Boolean lida,
        Long eventoId,
        Long rodizioId,
        Long ordemCorridaId,
        LocalDateTime dataCriacao,
        LocalDateTime dataLeitura
) {
}
