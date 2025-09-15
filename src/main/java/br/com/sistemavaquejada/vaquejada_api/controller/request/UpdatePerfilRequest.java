package br.com.sistemavaquejada.vaquejada_api.controller.request;

import br.com.sistemavaquejada.vaquejada_api.entity.Enumns.Perfil;
import jakarta.validation.constraints.NotNull;

public record UpdatePerfilRequest(@NotNull(message = "Perfil é obrigatório")
                                  Perfil perfil) {
}
