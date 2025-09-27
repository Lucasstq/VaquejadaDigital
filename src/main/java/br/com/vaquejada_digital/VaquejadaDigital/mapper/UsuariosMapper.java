package br.com.vaquejada_digital.VaquejadaDigital.mapper;

import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.UsuarioSimpleResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.UsuariosResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.UsuariosRequest;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UsuariosMapper {

    public static UsuarioSimpleResponse toUsuarioSimpleResponse(Usuarios usuario) {
        return UsuarioSimpleResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .build();
    }

    public static Usuarios toUser(UsuariosRequest request) {
        return Usuarios
                .builder()
                .email(request.email())
                .senha(request.senha())
                .nome(request.nome())
                .telefone(request.telefone())
                .tipoPerfil(request.perfil())
                .build();

    }

    public static UsuariosResponse toUserResponse(Usuarios usuario) {
        return UsuariosResponse
                .builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .telefone(usuario.getTelefone())
                .perfil(usuario.getTipoPerfil())
                .build();
    }

}
