package com.br.vaquejadadigital.dtos.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record CorredorResponse(
        Long id,
        Long usuarioId,
        String nomeUsuario,
        String cpf,
        String apelido,
        String telefone,
        String cidade,
        String estado,
        String fotoUrl,
        LocalDate dataNascimento,
        LocalDateTime dataCriacao
) {}