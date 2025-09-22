package br.com.vaquejada_digital.VaquejadaDigital.mapper;

import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.CorredorResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.CorredorRequest;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Corredor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CorredorMapper {

    public static Corredor toCorredor(CorredorRequest request) {
        return Corredor
                .builder()
                .nomeCompleto(request.nomeCompleto())
                .cpf(request.cpf())
                .telefone(request.telefone())
                .cidade(request.cidade())
                .fotoPerfil(request.fotoPerfil())
                .apelido(request.apelido())
                .build();
    }

    public static CorredorResponse toCorredorResponse(Corredor corredor) {
        return CorredorResponse
                .builder()
                .id(corredor.getId())
                .nomeCompleto(corredor.getNomeCompleto())
                .apelido(corredor.getApelido())
                .telefone(corredor.getTelefone())
                .cidade(corredor.getCidade())
                .build();
    }

}
