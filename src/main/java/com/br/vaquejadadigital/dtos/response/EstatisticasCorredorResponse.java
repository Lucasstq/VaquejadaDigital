package com.br.vaquejadadigital.dtos.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record EstatisticasCorredorResponse(
        Long corredorId,
        String nomeCompleto,
        String apelido,
        Integer totalEventos,
        Integer totalCorridas,
        Integer totalValeBoi,
        Integer totalZeros,
        Integer totalRetornos,
        Integer totalFaltas,
        Integer totalDesclassificacoes,
        BigDecimal taxaSucessoGeral,
        Integer melhorPosicao,
        String eventoMelhorPosicao
) {
}