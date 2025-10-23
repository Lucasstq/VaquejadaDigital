package com.br.vaquejadadigital.mapper;

import com.br.vaquejadadigital.dtos.request.RegisterRequest;
import com.br.vaquejadadigital.dtos.response.LoginResponse;
import com.br.vaquejadadigital.dtos.response.UsuarioResponse;
import com.br.vaquejadadigital.entities.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {


    public Usuario toUser(RegisterRequest request) {
        return Usuario
                .builder()
                .email(request.email())
                .senha(request.senha())
                .nome(request.nome())
                .perfil(request.perfil())
                .build();
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getAtivo(),
                usuario.getDataCriacao()
        );
    }

}
