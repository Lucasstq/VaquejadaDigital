package com.br.vaquejadadigital.dtos.request;

import com.br.vaquejadadigital.entities.enums.FaseRodizio;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RodizioRequest(
        @NotNull(message = "ID do evento é obrigatório")
        Long eventoId,

        @NotNull(message = "Número do rodízio é obrigatório")
        @Min(value = 1, message = "Número deve ser maior que zero")
        Integer numeroRodizio,

        @NotNull(message = "Fase é obrigatória")
        FaseRodizio fase
) {}
