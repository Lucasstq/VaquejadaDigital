package br.com.vaquejada_digital.VaquejadaDigital.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CorredorRequest(
        @NotBlank(message = "Nome completo é obrigatório")
        String nomeCompleto,
        String apelido,
        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato 000.000.000-00")
        String cpf,
        String telefone,
        String cidade,
        String fotoPerfil
) {
}
