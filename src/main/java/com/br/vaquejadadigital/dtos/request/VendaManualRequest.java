package com.br.vaquejadadigital.dtos.request;

import com.br.vaquejadadigital.entities.enums.FormaPagamento;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record VendaManualRequest(
        @NotNull(message = "ID do evento é obrigatório")
        Long eventoId,

        @NotEmpty(message = "Deve haver pelo menos uma senha")
        List<ItemVendaRequest> itens,

        @NotNull(message = "Forma de pagamento é obrigatória")
        FormaPagamento formaPagamento,

        String observacao
) {}
