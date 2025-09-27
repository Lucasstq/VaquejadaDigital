package br.com.vaquejada_digital.VaquejadaDigital.controller.reponse;

import lombok.Builder;

@Builder
public record UsuarioSimpleResponse(Long id, String nome, String email, String telefone) {
}
