package com.br.vaquejadadigital.dtos.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SenhaRequest(
        @NotNull(message = "ID do evento é obrigatório")
        Long eventoId,

        @NotNull(message = "ID da categoria é obrigatório")
        Long categoriaId,

        @NotNull(message = "Número da senha é obrigatório")
        @Min(value = 1, message = "Número da senha deve ser maior que zero")
        Integer numeroSenha,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        BigDecimal valor
) {}