package br.com.sistemavaquejada.vaquejada_api.controller.response;

import br.com.sistemavaquejada.vaquejada_api.entity.Status;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record EventResponse(
        Long id,
        String nome,
        LocalDate dataInicio,
        LocalDate dataFim,
        String local,
        String descricao,
        Double precoBaseSenha,
        Integer quantidadeTotalDeSenha,
        List<String> imagensVideos,
        Status status) {
}
