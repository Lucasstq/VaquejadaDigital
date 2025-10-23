package com.br.vaquejadadigital.dtos.response;

import com.br.vaquejadadigital.entities.enums.FaseRodizio;
import com.br.vaquejadadigital.entities.enums.StatusRodizio;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RodizioResponse(
        Long id,
        Long eventoId,
        String nomeEvento,
        Integer numeroRodizio,
        FaseRodizio fase,
        StatusRodizio status,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Integer totalDuplas,
        LocalDateTime dataCriacao
) {
}
