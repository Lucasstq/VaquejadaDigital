package com.br.vaquejadadigital.dtos.response;

import com.br.vaquejadadigital.entities.enums.TipoCategoria;
import lombok.Builder;

@Builder
public record CategoriaResponse(Long id, TipoCategoria nome, String descricao, Boolean ativo) {
}
