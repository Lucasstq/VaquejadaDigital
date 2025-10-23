package com.br.vaquejadadigital.dtos.response;

import com.br.vaquejadadigital.entities.enums.TipoResultado;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ResultadoResponse(
        Long id,
        Long ordemCorridaId,
        String nomeDupla,
        TipoResultado tipoResultado,
        String observacao,
        String nomeJuiz,
        BigDecimal tempoCorrida,
        LocalDateTime dataRegistro
) {}