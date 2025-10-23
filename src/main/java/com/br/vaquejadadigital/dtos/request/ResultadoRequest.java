package com.br.vaquejadadigital.dtos.request;

import com.br.vaquejadadigital.entities.enums.TipoResultado;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ResultadoRequest(
        @NotNull(message = "ID da ordem de corrida é obrigatório")
        Long ordemCorridaId,

        @NotNull(message = "Tipo de resultado é obrigatório")
        TipoResultado tipoResultado,

        String observacao,

        BigDecimal tempoCorrida
) {
}