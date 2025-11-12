package com.br.vaquejadadigital.dtos.response;

import com.br.vaquejadadigital.entities.enums.TipoCategoria;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record HistoricoCorredorResponse(
        Long id,
        Long corredorId,
        String nomeCompleto,
        String apelido,
        Long eventoId,
        String nomeEvento,
        TipoCategoria categoria,
        Integer totalCorridas,
        Integer valeBoi,
        Integer zeros,
        Integer retornos,
        Integer faltas,
        Integer desclassificacoes,
        BigDecimal pontuacaoTotal,
        Integer posicaoFinal,
        String colocacaoGeral,
        String melhorTempo,
        BigDecimal taxaSucesso,
        String observacoes,
        LocalDateTime dataCriacao
) {
}