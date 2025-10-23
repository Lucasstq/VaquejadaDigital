package com.br.vaquejadadigital.dtos.request;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record EventoRequest(
        @NotBlank(message = "Nome do evento é obrigatório")
        @Size(max = 200, message = "Nome deve ter no máximo 200 caracteres")
        String nome,

        String descricao,

        @NotBlank(message = "Local é obrigatório")
        @Size(max = 200, message = "Local deve ter no máximo 200 caracteres")
        String local,

        @NotNull(message = "Data de início é obrigatória")
        @Future(message = "Data de início deve ser futura")
        LocalDateTime dataInicio,

        @NotNull(message = "Data de fim é obrigatória")
        LocalDateTime dataFim,

        @NotNull(message = "Quantidade total de senhas é obrigatória")
        @Min(value = 1, message = "Deve haver pelo menos 1 senha")
        Integer quantidadeSenhasTotal,

        @NotNull(message = "Valor da senha é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        BigDecimal valorSenha,

        @Min(value = 1, message = "Deve haver pelo menos 1 dupla por rodízio")
        @Max(value = 50, message = "Máximo de 50 duplas por rodízio")
        Integer limiteDuplasPorRodizio
) {
}
