package br.com.vaquejada_digital.VaquejadaDigital.controller.request;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record EventoRequest(
        @NotBlank(message = "Nome do evento é obrigatório")
        String nome,

        @NotNull(message = "Data de início é obrigatória")
        @FutureOrPresent(message = "Data de início deve ser hoje ou no futuro")
        LocalDate dataInicio,

        @NotNull(message = "Data de fim é obrigatória")
        LocalDate dataFim,

        @NotBlank(message = "Local é obrigatório")
        String local,

        String descricao,

        List<String> imagensVideos,

        @NotNull(message = "Valor da senha é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor da senha deve ser maior que zero")
        BigDecimal precoBaseSenha,

        @NotNull(message = "Quantidade de senhas é obrigatória")
        @Min(value = 1, message = "Deve haver pelo menos 1 senha disponível")
        Integer quantidadeTotalSenhas,

        @NotEmpty(message = "Deve haver pelo menos um juiz designado")
        List<Long> juizesIds,

        @NotEmpty(message = "Deve haver pelo menos um locutor designado")
        List<Long> locutoresIds
) {}
