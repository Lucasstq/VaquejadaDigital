package com.br.vaquejadadigital.dtos.response;

import java.util.List;

public record MapaSenhasResponse(
        Long eventoId,
        String nomeEvento,
        List<CategoriaSenhasResponse> categorias
) {}
