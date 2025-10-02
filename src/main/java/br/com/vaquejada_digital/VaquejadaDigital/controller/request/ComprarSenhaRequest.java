package br.com.vaquejada_digital.VaquejadaDigital.controller.request;

import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.FormaPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComprarSenhaRequest(
        @NotNull(message = "ID do evento é obrigatório")
        Long eventoId,

        @NotNull(message = "Número da senha é obrigatório")
        Integer numeroSenha,

        @NotNull(message = "ID da categoria é obrigatório")
        Long categoriaId,

        @NotNull(message = "ID do puxador é obrigatório")
        Long puxadorId,

        @NotNull(message = "ID do esteireiro é obrigatório")
        Long esteireiroId,

        String nomeDupla,

        @NotBlank(message = "Dia de corrida é obrigatório")
        String diaCorrida,

        @NotNull(message = "Forma de pagamento é obrigatória")
        FormaPagamento formaPagamento,

        @NotNull(message = "Informe se pagou com desconto")
        Boolean comDesconto
) {}
