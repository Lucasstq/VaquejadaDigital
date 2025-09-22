package br.com.vaquejada_digital.VaquejadaDigital.controller.reponse;

import lombok.Builder;

@Builder
public record CorredorResponse(Long id, String nomeCompleto, String apelido, String telefone, String cidade) {
}
