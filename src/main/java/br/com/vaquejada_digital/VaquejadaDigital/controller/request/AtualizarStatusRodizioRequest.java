package br.com.vaquejada_digital.VaquejadaDigital.controller.request;

import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.StatusRodizio;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRodizioRequest(
        @NotNull(message = "Status é obrigatório")
        StatusRodizio status) {
}
