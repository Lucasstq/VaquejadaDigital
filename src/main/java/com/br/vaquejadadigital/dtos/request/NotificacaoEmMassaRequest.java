package com.br.vaquejadadigital.dtos.request;

import com.br.vaquejadadigital.entities.enums.TipoNotificacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificacaoEmMassaRequest(
        @NotNull(message = "Evento ID é obrigatório")
        Long eventoId,

        @NotBlank(message = "Mensagem é obrigatória")
        String mensagem,

        @NotNull(message = "Tipo de notificação é obrigatório")
        TipoNotificacao tipo
) {
}
