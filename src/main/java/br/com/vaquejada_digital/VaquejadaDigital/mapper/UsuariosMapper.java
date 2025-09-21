package br.com.vaquejada_digital.VaquejadaDigital.mapper;

import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.UsuariosResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.UsuariosRequest;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UsuariosMapper {

    public static Usuarios toUser(UsuariosRequest request) {
        return Usuarios
                .builder()
                .email(request.email())
                .senha(request.senha())
                .nome(request.nome())
                .telefone(request.telefone())
                .build();

    }

    public static UsuariosResponse toUserResponse(Usuarios usuario) {
        return UsuariosResponse
                .builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .telefone(usuario.getTelefone())
                .build();
    }

}
