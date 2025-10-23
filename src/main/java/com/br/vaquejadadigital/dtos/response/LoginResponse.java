package com.br.vaquejadadigital.dtos.response;

import lombok.Builder;

@Builder
public record LoginResponse(String token) {
}
