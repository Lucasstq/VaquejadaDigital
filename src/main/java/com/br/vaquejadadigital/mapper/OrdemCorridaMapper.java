package com.br.vaquejadadigital.mapper;

import com.br.vaquejadadigital.dtos.response.OrdemCorridaResponse;
import com.br.vaquejadadigital.entities.OrdemCorrida;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrdemCorridaMapper {

    public OrdemCorridaResponse toResponse(OrdemCorrida ordem) {
        return new OrdemCorridaResponse(
                ordem.getId(),
                ordem.getRodizio().getId(),
                ordem.getDupla().getId(),
                ordem.getDupla().getPuxador().getUsuario().getNome(),
                ordem.getDupla().getEsteireiro().getUsuario().getNome(),
                ordem.getPosicao(),
                ordem.getStatus(),
                ordem.getDataChamada(),
                ordem.getDataCorrida()
        );
    }

}
