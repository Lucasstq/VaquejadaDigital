package br.com.sistemavaquejada.vaquejada_api.controller.response;

import br.com.sistemavaquejada.vaquejada_api.entity.Enumns.Perfil;
import lombok.Builder;

@Builder
public record UserResponse(String email, String telefone, Perfil perfil) {
}
