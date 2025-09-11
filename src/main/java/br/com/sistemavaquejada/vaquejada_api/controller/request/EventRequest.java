package br.com.sistemavaquejada.vaquejada_api.controller.request;

import br.com.sistemavaquejada.vaquejada_api.entity.Status;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record EventRequest(
        @NotBlank(message = "Esse campo é obrigatório.")
        String nome,
        @NotNull(message = "Esse campo é obrigatório.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dataInicio,
        @NotNull(message = "Esse campo é obrigatório.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dataFim,
        @NotBlank(message = "Campo local é obrigatório.")
        String local,
        @NotBlank(message = "Esse campo é obrigatório.")
        String descricao,
        @NotNull(message = "Esse campo é obrigatório.")
        @Positive(message = "O preço deve ser maior que zero")
        Double precoBaseSenha,
        @NotNull(message = "Esse campo é obrigatório")
        Integer quantidadeTotalDeSenha,
        @JsonAlias({"evento_midias", "imagensVideos"})
        List<String> imagensVideos,
        Status status) {
}
