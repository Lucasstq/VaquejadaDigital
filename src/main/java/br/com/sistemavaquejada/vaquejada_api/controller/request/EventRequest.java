package br.com.sistemavaquejada.vaquejada_api.controller.request;

import br.com.sistemavaquejada.vaquejada_api.entity.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record EventRequest(String nome,
                           @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                           LocalDate dataInicio,
                           @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                           LocalDate dataFim,
                           String local,
                           String descricao,
                           Double precoBaseSenha,
                           Integer quantidadeTotalDeSenha,
                           List<String> imagensVideos,
                           Status status) {
}
