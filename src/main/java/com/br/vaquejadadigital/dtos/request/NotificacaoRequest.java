package com.br.vaquejadadigital.dtos.request;

import com.br.vaquejadadigital.entities.enums.TipoNotificacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NotificacaoRequest(
        @NotNull(message = "Usuário ID é obrigatório")
        Long usuarioId,

        @NotBlank(message = "Título é obrigatório")
        @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
        String titulo,

        @NotBlank(message = "Mensagem é obrigatória")
        String mensagem,

        @NotNull(message = "Tipo de notificação é obrigatório")
        TipoNotificacao tipo,

        Long eventoId,
        Long rodizioId,
        Long ordemCorridaId
) {
}
