package br.com.sistemavaquejada.vaquejada_api.controller.request;

import br.com.sistemavaquejada.vaquejada_api.entity.Status;
import lombok.Builder;
import java.util.List;

@Builder
public record EventRequest(String nome,
                           String local,
                           String descricao,
                           Double precoBaseSenha,
                           Integer quantidadeTotalDeSenha,
                           List<String> imagensVideos,
                           Status status) {
}
