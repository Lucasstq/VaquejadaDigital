package br.com.vaquejada_digital.VaquejadaDigital.mapper;

import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.OrdemCorridaResponse;
import br.com.vaquejada_digital.VaquejadaDigital.entity.OrdemCorrida;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrdemCorridaMapper {

    public static OrdemCorridaResponse toResponse(OrdemCorrida ordem) {
        return OrdemCorridaResponse.builder()
                .id(ordem.getId())
                .rodizioId(ordem.getRodizio().getId())
                .posicao(ordem.getPosicao())
                .numeroSenha(ordem.getSenha().getNumeroSenha())
                .nomeDupla(ordem.getSenha().getDupla() != null ?
                        ordem.getSenha().getDupla().getNomeDuplaFormatado() : null)
                .nomePuxador(ordem.getSenha().getDupla() != null ?
                        ordem.getSenha().getDupla().getPuxador().getNomeCompleto() : null)
                .nomeEsteireiro(ordem.getSenha().getDupla() != null ?
                        ordem.getSenha().getDupla().getEsteireiro().getNomeCompleto() : null)
                .statusChamada(ordem.getStatusChamada())
                .resultado(ordem.getResultado())
                .observacoes(ordem.getObservacoes())
                .nomeJuiz(ordem.getJuiz() != null ? ordem.getJuiz().getNome() : null)
                .horarioChamada(ordem.getHorarioChamada())
                .horarioCorrida(ordem.getHorarioCorrida())
                .build();
    }
}
