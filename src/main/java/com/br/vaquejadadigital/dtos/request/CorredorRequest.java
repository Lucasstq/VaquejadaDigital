package com.br.vaquejadadigital.dtos.request;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CorredorRequest(
        String cpf,
        String apelido,
        String telefone,
        String cidade,
        String estado,
        String fotoUrl,
        LocalDate dataNascimento
) {
}
