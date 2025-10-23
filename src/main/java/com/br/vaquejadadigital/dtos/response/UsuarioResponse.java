package com.br.vaquejadadigital.dtos.response;

import com.br.vaquejadadigital.entities.enums.Perfil;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        Perfil perfil,
        Boolean ativo,
        LocalDateTime dataCriacao
) {
}