package com.br.vaquejadadigital.mapper;

import com.br.vaquejadadigital.dtos.request.CorredorRequest;
import com.br.vaquejadadigital.dtos.response.CorredorResponse;
import com.br.vaquejadadigital.entities.Corredor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CorredorMapper {

    public Corredor toEntity(CorredorRequest request) {
        return Corredor.builder()
                .cpf(request.cpf())
                .apelido(request.apelido())
                .telefone(request.telefone())
                .cidade(request.cidade())
                .estado(request.estado())
                .fotoUrl(request.fotoUrl())
                .dataNascimento(request.dataNascimento())
                .build();
    }

    public CorredorResponse toResponse(Corredor corredor) {
        return new CorredorResponse(
                corredor.getId(),
                corredor.getUsuario() != null ? corredor.getUsuario().getId() : null,
                corredor.getUsuario() != null ? corredor.getUsuario().getNome() : null,
                corredor.getCpf(),
                corredor.getApelido(),
                corredor.getTelefone(),
                corredor.getCidade(),
                corredor.getEstado(),
                corredor.getFotoUrl(),
                corredor.getDataNascimento(),
                corredor.getDataCriacao()
        );
    }

}
