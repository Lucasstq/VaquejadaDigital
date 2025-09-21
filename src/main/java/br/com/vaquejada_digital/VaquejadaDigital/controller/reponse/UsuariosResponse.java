package br.com.vaquejada_digital.VaquejadaDigital.controller.reponse;

import lombok.Builder;

@Builder
public record UsuariosResponse(Long id, String email, String nome, String telefone) {
}
