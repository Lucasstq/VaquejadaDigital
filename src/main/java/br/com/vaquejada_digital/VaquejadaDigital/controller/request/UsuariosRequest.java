package br.com.vaquejada_digital.VaquejadaDigital.controller.request;

import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.Perfil;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UsuariosRequest(@NotBlank(message = "Campo obrigatório")
                              String nome,
                              @NotBlank(message = "Campo obrigatório")
                              String email,
                              @NotBlank(message = "Campo obrigatório")
                              String senha,
                              String telefone,
                              Perfil perfil) {
}
