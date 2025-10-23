package com.br.vaquejadadigital.dtos.response;

import java.util.List;

public record CategoriaSenhasResponse(
        Long categoriaId,
        String nomeCategoria,
        List<SenhaMapaResponse> senhas
) {}
