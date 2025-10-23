package com.br.vaquejadadigital.dtos.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ItemVendaRequest(
        @NotNull(message = "ID da senha é obrigatório")
        Long senhaId,

        @NotNull(message = "ID do puxador é obrigatório")
        Long puxadorId,

        @NotNull(message = "ID do esteireiro é obrigatório")
        Long esteireiroId,

        @NotNull(message = "O valor é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
        BigDecimal valor
) {
}
