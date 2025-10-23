package com.br.vaquejadadigital.dtos.response;

import com.br.vaquejadadigital.entities.enums.StatusSenha;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record SenhaResponse(
        Long id,
        Long eventoId,
        String nomeEvento,
        Long categoriaId,
        String nomeCategoria,
        Integer numeroSenha,
        StatusSenha status,
        BigDecimal valor,
        DuplaSimplificadaResponse dupla,
        LocalDateTime dataCriacao
) {}
