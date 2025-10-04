package br.com.vaquejada_digital.VaquejadaDigital.controller.request;

import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.ResultadoCorrida;
import jakarta.validation.constraints.NotNull;

public record RegistrarResultadoRequest(
        @NotNull(message = "ID da ordem de corrida é obrigatório")
        Long ordemCorridaId,

        @NotNull(message = "Resultado é obrigatório")
        ResultadoCorrida resultado
        ) {}
