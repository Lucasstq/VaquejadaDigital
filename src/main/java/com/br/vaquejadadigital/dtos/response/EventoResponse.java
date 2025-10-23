package com.br.vaquejadadigital.dtos.response;

import com.br.vaquejadadigital.entities.enums.StatusEvento;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record EventoResponse(
        Long id,
        String nome,
        String descricao,
        String local,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Integer quantidadeSenhasTotal,
        Integer quantidadeSenhasVendidas,
        Integer quantidadeSenhasDisponiveis,
        BigDecimal valorSenha,
        Integer limiteDuplasPorRodizio,
        StatusEvento statusEvento
) {
}