package com.br.vaquejadadigital.mapper;

import com.br.vaquejadadigital.dtos.response.CategoriaResponse;
import com.br.vaquejadadigital.entities.Categoria;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoriaMapper {

    public CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getAtivo()
        );
    }

}
