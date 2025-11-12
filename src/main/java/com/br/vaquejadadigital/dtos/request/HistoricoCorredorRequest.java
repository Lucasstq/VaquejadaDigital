package com.br.vaquejadadigital.dtos.request;

import com.br.vaquejadadigital.entities.enums.TipoCategoria;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record HistoricoCorredorRequest(
        @NotNull(message = "Corredor ID é obrigatório")
        Long corredorId,

        @NotNull(message = "Evento ID é obrigatório")
        Long eventoId,

        @NotNull(message = "Categoria é obrigatória")
        TipoCategoria categoria,

        @Min(value = 0, message = "Total de corridas não pode ser negativo")
        Integer totalCorridas,

        @Min(value = 0, message = "Vale o boi não pode ser negativo")
        Integer valeBoi,

        @Min(value = 0, message = "Zeros não pode ser negativo")
        Integer zeros,

        @Min(value = 0, message = "Retornos não pode ser negativo")
        Integer retornos,

        @Min(value = 0, message = "Faltas não pode ser negativo")
        Integer faltas,

        @Min(value = 0, message = "Desclassificações não pode ser negativo")
        Integer desclassificacoes,

        BigDecimal pontuacaoTotal,

        Integer posicaoFinal,

        String colocacaoGeral,

        String melhorTempo,

        String observacoes
) {
}
