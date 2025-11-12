package com.br.vaquejadadigital.dtos.request;

import com.br.vaquejadadigital.entities.enums.TipoCategoria;
import com.br.vaquejadadigital.entities.enums.TipoResultado;
import lombok.Builder;

@Builder
public record RelatorioFiltroRequest(
        Long eventoId,
        TipoCategoria categoria,
        Long corredorId,
        Long rodizioId,
        TipoResultado tipoResultado,
        String formato
) {
}
