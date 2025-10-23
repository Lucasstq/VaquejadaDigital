package com.br.vaquejadadigital.dtos.response;

import com.br.vaquejadadigital.entities.enums.StatusCorrida;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrdemCorridaResponse(
        Long id,
        Long rodizioId,
        Long duplaId,
        String nomePuxador,
        String nomeEsteireiro,
        Integer posicao,
        StatusCorrida status,
        LocalDateTime dataChamada,
        LocalDateTime dataCorrida
) {}
