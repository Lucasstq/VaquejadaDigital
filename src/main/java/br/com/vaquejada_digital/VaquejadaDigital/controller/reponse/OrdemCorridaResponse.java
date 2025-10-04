package br.com.vaquejada_digital.VaquejadaDigital.controller.reponse;

import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.ResultadoCorrida;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.StatusChamada;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrdemCorridaResponse(Long id,
                                   Long rodizioId,
                                   Integer posicao,
                                   Integer numeroSenha,
                                   String nomeDupla,
                                   String nomePuxador,
                                   String nomeEsteireiro,
                                   StatusChamada statusChamada,
                                   ResultadoCorrida resultado,
                                   String observacoes,
                                   String nomeJuiz,
                                   LocalDateTime horarioChamada,
                                   LocalDateTime horarioCorrida) {
}
