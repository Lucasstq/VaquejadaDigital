package com.br.vaquejadadigital.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OrdemCorridaRequest(
        @NotNull(message = "ID do rodízio é obrigatório")
        Long rodizioId,

        @NotNull(message = "ID da dupla é obrigatório")
        Long duplaId,

        @NotNull(message = "Posição é obrigatória")
        @Min(value = 1, message = "Posição deve ser maior que zero")
        Integer posicao
) {}
