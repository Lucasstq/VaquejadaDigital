package br.com.vaquejada_digital.VaquejadaDigital.controller.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message = "campo obrigatório.")
                           String email,
                           @NotBlank(message = "campo obrigatório.")
                           String senha) {
}
