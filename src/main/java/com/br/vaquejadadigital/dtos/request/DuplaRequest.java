package com.br.vaquejadadigital.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DuplaRequest(
        @NotNull(message = "ID do puxador é obrigatório")
        Long puxadorId,

        @NotNull(message = "ID do esteireiro é obrigatório")
        Long esteireiroId,

        String nomeDupla
) {
}