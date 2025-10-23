package com.br.vaquejadadigital.config;

import lombok.Builder;

@Builder
public record JWTUserData(Long id, String email) {
}
