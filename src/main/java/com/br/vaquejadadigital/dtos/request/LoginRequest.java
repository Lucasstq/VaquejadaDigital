package com.br.vaquejadadigital.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(@NotBlank(message = "Email é obrigatório")
                           @Email(message = "Email inválido")
                           String email,

                           @NotBlank(message = "Senha é obrigatória")
                           String senha) {
}
