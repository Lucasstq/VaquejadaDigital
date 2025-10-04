package br.com.vaquejada_digital.VaquejadaDigital.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CriarRodizioAutomaticoRequest(@NotNull(message = "ID do evento é obrigatório")
                             Long eventoId,

                                            @NotNull(message = "ID da categoria é obrigatório")
                             Long categoriaId,

                                            @NotNull(message = "Tamanho do rodízio é obrigatório")
                             @Min(value = 1, message = "Tamanho mínimo é 1")
                             Integer tamanhoRodizio) {
}
