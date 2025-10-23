package com.br.vaquejadadigital.dtos.request;

import com.br.vaquejadadigital.entities.enums.FormaPagamento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record VendaOnlineRequest(
        @NotNull(message = "ID do evento é obrigatório")
        Long eventoId,

        @NotNull(message = "O ID do comprador é obrigatório")
        Long compradorId,

        @NotNull(message = "O valor total é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor total deve ser maior que zero")
        BigDecimal valorTotal,

        @NotEmpty(message = "Deve haver pelo menos uma senha")
        List<ItemVendaRequest> itens,

        @NotNull(message = "Forma de pagamento é obrigatória")
        FormaPagamento formaPagamento
) {
}