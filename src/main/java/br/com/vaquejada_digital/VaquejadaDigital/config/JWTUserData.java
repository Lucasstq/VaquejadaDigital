package br.com.vaquejada_digital.VaquejadaDigital.config;

import lombok.Builder;

@Builder
public record JWTUserData(Long id, String email) {
}
