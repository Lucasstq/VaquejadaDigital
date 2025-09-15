package br.com.sistemavaquejada.vaquejada_api.controller.request;

import br.com.sistemavaquejada.vaquejada_api.entity.Enumns.Perfil;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserRequest(
        @NotBlank(message = "Campo obrigatório")
        String nome,
        @NotBlank(message = "Campo obrigatório")
        String email,
        @NotBlank(message = "Campo obrigatório")
        String senha,
        @NotBlank(message = "Campo obrigatório")
        String telefone,
        Perfil perfil) {
}
