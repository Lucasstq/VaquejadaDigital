package br.com.vaquejada_digital.VaquejadaDigital.controller.reponse;

import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.StatusRodizio;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.TipoRodizio;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record RodizioResponse(Long id,
                              Long eventoId,
                              String nomeEvento,
                              Long categoriaId,
                              String nomeCategoria,
                              Integer numeroRodizio,
                              TipoRodizio tipoRodizio,
                              StatusRodizio status,
                              Integer quantidadeDuplas,
                              List<OrdemCorridaResponse> ordens,  // Lista de duplas neste rodízio
                              LocalDateTime criadoEm) {
}
