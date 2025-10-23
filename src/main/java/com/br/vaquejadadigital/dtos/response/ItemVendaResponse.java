package com.br.vaquejadadigital.dtos.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ItemVendaResponse(
        Long id,
        Long senhaId,
        Integer numeroSenha,
        String categoria,
        String nomePuxador,
        String nomeEsteireiro,
        BigDecimal valor
) {
}
