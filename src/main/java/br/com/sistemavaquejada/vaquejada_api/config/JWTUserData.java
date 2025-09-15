package br.com.sistemavaquejada.vaquejada_api.config;

import lombok.Builder;

@Builder
public record JWTUserData(Long id, String email) {
}
