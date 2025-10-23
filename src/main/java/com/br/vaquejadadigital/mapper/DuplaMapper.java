package com.br.vaquejadadigital.mapper;

import com.br.vaquejadadigital.dtos.response.DuplaResponse;
import com.br.vaquejadadigital.dtos.response.DuplaSimplificadaResponse;
import com.br.vaquejadadigital.entities.Dupla;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DuplaMapper {

    public DuplaResponse toResponse(Dupla dupla) {
        return new DuplaResponse(
                dupla.getId(),
                dupla.getPuxador().getId(),
                dupla.getPuxador().getUsuario().getNome(),
                dupla.getEsteireiro().getId(),
                dupla.getEsteireiro().getUsuario().getNome(),
                dupla.getNomeDupla(),
                dupla.getDataCriacao()
        );
    }

    public DuplaSimplificadaResponse toSimplificada(Dupla dupla) {
        if (dupla == null) return null;

        return new DuplaSimplificadaResponse(
                dupla.getId(),
                dupla.getPuxador().getUsuario().getNome(),
                dupla.getEsteireiro().getUsuario().getNome(),
                dupla.getNomeDupla()
        );
    }

}
