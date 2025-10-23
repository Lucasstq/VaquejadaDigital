package com.br.vaquejadadigital.dtos.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DuplaResponse(
        Long id,
        Long puxadorId,
        String nomePuxador,
        Long esteireiroId,
        String nomeEsteireiro,
        String nomeDupla,
        LocalDateTime dataCriacao
) {
}