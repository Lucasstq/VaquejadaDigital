package br.com.vaquejada_digital.VaquejadaDigital.controller.reponse;

import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.Perfil;
import lombok.Builder;

@Builder
public record UsuariosResponse(Long id, String email, String nome, String telefone, Perfil perfil) {
}
