package com.br.vaquejadadigital.mapper;

import com.br.vaquejadadigital.dtos.response.RodizioResponse;
import com.br.vaquejadadigital.entities.Rodizio;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RodizioMapper {

    public RodizioResponse toResponse(Rodizio rodizio) {
        return new RodizioResponse(
                rodizio.getId(),
                rodizio.getEvento().getId(),
                rodizio.getEvento().getNome(),
                rodizio.getNumeroRodizio(),
                rodizio.getFase(),
                rodizio.getStatus(),
                rodizio.getDataInicio(),
                rodizio.getDataFim(),
                rodizio.getOrdemCorridas() != null ? rodizio.getOrdemCorridas().size() : 0,
                rodizio.getDataCriacao()
        );
    }

}
