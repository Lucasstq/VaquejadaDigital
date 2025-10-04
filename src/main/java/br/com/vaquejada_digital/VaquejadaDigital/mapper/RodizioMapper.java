package br.com.vaquejada_digital.VaquejadaDigital.mapper;

import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.OrdemCorridaResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.RodizioResponse;
import br.com.vaquejada_digital.VaquejadaDigital.entity.OrdemCorrida;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Rodizio;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RodizioMapper {


    public static RodizioResponse toRodizioResponse(Rodizio rodizio) {
        return RodizioResponse
                .builder()
                .id(rodizio.getId())
                .eventoId(rodizio.getEvento().getId())
                .nomeEvento(rodizio.getEvento().getNome())
                .categoriaId(rodizio.getCategoria().getId())
                .nomeCategoria(rodizio.getCategoria().getDescricao())
                .numeroRodizio(rodizio.getNumeroRodizio())
                .tipoRodizio(rodizio.getTipoRodizio())
                .status(rodizio.getStatus())
                .quantidadeDuplas(rodizio.getQuantidadeDuplas())
                .ordens(rodizio.getOrdens().stream()
                        .map(RodizioMapper::toOrdemCorridaResponse)
                        .toList())
                .criadoEm(rodizio.getCriadoEm())
                .build();
    }


    public static OrdemCorridaResponse toOrdemCorridaResponse(OrdemCorrida ordem) {
        return OrdemCorridaResponse
                .builder()
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
