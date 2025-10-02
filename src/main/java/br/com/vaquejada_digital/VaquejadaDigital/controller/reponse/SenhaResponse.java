package br.com.vaquejada_digital.VaquejadaDigital.controller.reponse;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SenhaResponse(
        Long id,
        Integer numeroSenha,
        Boolean disponivel,
        Boolean bloqueada,
        String diaCorrida,
        BigDecimal valorPago,
        Boolean pagouComDesconto,
        String nomeDupla,
        String statusPagamento
) {}
