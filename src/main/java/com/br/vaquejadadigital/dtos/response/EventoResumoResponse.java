package com.br.vaquejadadigital.dtos.response;

import com.br.vaquejadadigital.entities.enums.StatusEvento;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record EventoResumoResponse(
        Long id,
        String nome,
        String local,
        LocalDateTime dataInicio,
        StatusEvento status,
        Integer senhasDisponiveis,
        BigDecimal valorSenha
) {
}