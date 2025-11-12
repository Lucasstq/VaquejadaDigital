package com.br.vaquejadadigital.mapper;

import com.br.vaquejadadigital.dtos.request.HistoricoCorredorRequest;
import com.br.vaquejadadigital.dtos.response.HistoricoCorredorResponse;
import com.br.vaquejadadigital.entities.Corredor;
import com.br.vaquejadadigital.entities.Evento;
import com.br.vaquejadadigital.entities.HistoricoCorredor;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class HistoricoCorredorMapper {

    public static HistoricoCorredor toEntity(HistoricoCorredorRequest request, Corredor corredor, Evento evento) {
        HistoricoCorredor historico = HistoricoCorredor.builder()
                .corredor(corredor)
                .evento(evento)
                .categoria(request.categoria())
                .totalCorridas(request.totalCorridas() != null ? request.totalCorridas() : 0)
                .valeBoi(request.valeBoi() != null ? request.valeBoi() : 0)
                .zeros(request.zeros() != null ? request.zeros() : 0)
                .retornos(request.retornos() != null ? request.retornos() : 0)
                .faltas(request.faltas() != null ? request.faltas() : 0)
                .desclassificacoes(request.desclassificacoes() != null ? request.desclassificacoes() : 0)
                .pontuacaoTotal(request.pontuacaoTotal() != null ? request.pontuacaoTotal() : BigDecimal.ZERO)
                .posicaoFinal(request.posicaoFinal())
                .colocacaoGeral(request.colocacaoGeral())
                .melhorTempo(request.melhorTempo())
                .observacoes(request.observacoes())
                .build();

        historico.calcularTaxaSucesso();
        return historico;
    }

    public static HistoricoCorredorResponse toResponse(HistoricoCorredor historico) {
        return HistoricoCorredorResponse.builder()
                .id(historico.getId())
                .corredorId(historico.getCorredor().getId())
                .nomeCompleto(historico.getCorredor().getUsuario().getNome())
                .apelido(historico.getCorredor().getApelido())
                .eventoId(historico.getEvento().getId())
                .nomeEvento(historico.getEvento().getNome())
                .categoria(historico.getCategoria())
                .totalCorridas(historico.getTotalCorridas())
                .valeBoi(historico.getValeBoi())
                .zeros(historico.getZeros())
                .retornos(historico.getRetornos())
                .faltas(historico.getFaltas())
                .desclassificacoes(historico.getDesclassificacoes())
                .pontuacaoTotal(historico.getPontuacaoTotal())
                .posicaoFinal(historico.getPosicaoFinal())
                .colocacaoGeral(historico.getColocacaoGeral())
                .melhorTempo(historico.getMelhorTempo())
                .taxaSucesso(historico.getTaxaSucesso())
                .observacoes(historico.getObservacoes())
                .dataCriacao(historico.getDataCriacao())
                .build();
    }
}