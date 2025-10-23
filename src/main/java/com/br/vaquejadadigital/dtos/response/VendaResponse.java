package com.br.vaquejadadigital.dtos.response;

import com.br.vaquejadadigital.entities.enums.FormaPagamento;
import com.br.vaquejadadigital.entities.enums.StatusPagamento;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record VendaResponse(
        Long id,
        Long eventoId,
        String nomeEvento,
        Long compradorId,
        String nomeComprador,
        Long vendedorId,
        String nomeVendedor,
        FormaPagamento formaPagamento,
        StatusPagamento statusPagamento,
        BigDecimal valorTotal,
        String observacao,
        LocalDateTime dataVenda,
        List<ItemVendaResponse> itens
) {
}