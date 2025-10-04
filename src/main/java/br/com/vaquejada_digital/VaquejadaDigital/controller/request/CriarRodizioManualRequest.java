package br.com.vaquejada_digital.VaquejadaDigital.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CriarRodizioManualRequest(
        @NotNull(message = "ID do evento é obrigatório")
        Long eventoId,

        @NotNull(message = "ID da categoria é obrigatório")
        Long categoriaId,

        @NotEmpty(message = "Deve haver pelo menos uma senha no rodízio")
        @Size(max = 20, message = "Máximo de 20 duplas por rodízio")
        List<Long> senhaIds) {}
