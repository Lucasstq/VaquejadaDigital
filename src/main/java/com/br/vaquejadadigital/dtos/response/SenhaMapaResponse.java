package com.br.vaquejadadigital.dtos.response;

public record SenhaMapaResponse(
        Long id,
        Integer numeroSenha,
        String status,
        String nomeDupla
) {}
