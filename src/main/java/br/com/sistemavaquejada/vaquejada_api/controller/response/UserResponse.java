package br.com.sistemavaquejada.vaquejada_api.controller.response;

import br.com.sistemavaquejada.vaquejada_api.entity.Enumns.Perfil;
import br.com.sistemavaquejada.vaquejada_api.entity.User;
import lombok.Builder;

import java.util.List;

@Builder
public record UserResponse(Long id, String email, String nome, String telefone, Perfil perfil) {
}
