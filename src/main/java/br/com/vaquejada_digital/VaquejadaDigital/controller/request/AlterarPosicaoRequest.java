package br.com.vaquejada_digital.VaquejadaDigital.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AlterarPosicaoRequest(

        @NotNull(message = "ID da ordem de corrida é obrigatório")
        Long ordemCorridaId,

        @NotNull(message = "Nova posição é obrigatória")
        @Min(value = 1, message = "Posição mínima é 1")
        Integer novaPosicao
) {
}
