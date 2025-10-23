package com.br.vaquejadadigital.dtos.request;

import com.br.vaquejadadigital.entities.enums.StatusEvento;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record EventoUpdateRequest(
        String nome,
        String descricao,
        String local,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        BigDecimal valorSenha,
        Integer limiteDuplasPorRodizio,
        StatusEvento status
) {
}
