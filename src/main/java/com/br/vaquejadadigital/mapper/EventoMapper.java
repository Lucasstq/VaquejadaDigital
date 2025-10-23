package com.br.vaquejadadigital.mapper;

import com.br.vaquejadadigital.dtos.request.EventoRequest;
import com.br.vaquejadadigital.dtos.request.EventoUpdateRequest;
import com.br.vaquejadadigital.dtos.response.EventoResponse;
import com.br.vaquejadadigital.dtos.response.EventoResumoResponse;
import com.br.vaquejadadigital.entities.Evento;
import com.br.vaquejadadigital.entities.enums.StatusEvento;
import org.springframework.stereotype.Component;

@Component
public class EventoMapper {


    public Evento toEntity(EventoRequest request) {
        return Evento.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .local(request.local())
                .dataInicio(request.dataInicio())
                .dataFim(request.dataFim())
                .quantidadeSenhasTotal(request.quantidadeSenhasTotal())
                .valorSenha(request.valorSenha())
                .limiteDuplasPorRodizio(request.limiteDuplasPorRodizio() != null ? request.limiteDuplasPorRodizio() : 10)
                .quantidadeSenhasVendidas(0)
                .status(StatusEvento.CRIADO)
                .build();
    }

    public EventoResponse toResponse(Evento evento) {
        int senhasDisponiveis = evento.getQuantidadeSenhasTotal() - evento.getQuantidadeSenhasVendidas();

        return new EventoResponse(
                evento.getId(),
                evento.getNome(),
                evento.getDescricao(),
                evento.getLocal(),
                evento.getDataInicio(),
                evento.getDataFim(),
                evento.getQuantidadeSenhasTotal(),
                evento.getQuantidadeSenhasVendidas(),
                senhasDisponiveis,
                evento.getValorSenha(),
                evento.getLimiteDuplasPorRodizio(),
                evento.getStatus()
        );
    }

    public EventoResumoResponse toResumo(Evento evento) {
        int senhasDisponiveis = evento.getQuantidadeSenhasTotal() - evento.getQuantidadeSenhasVendidas();

        return new EventoResumoResponse(
                evento.getId(),
                evento.getNome(),
                evento.getLocal(),
                evento.getDataInicio(),
                evento.getStatus(),
                senhasDisponiveis,
                evento.getValorSenha()
        );
    }

    public Evento toEntityFromUpdate(EventoUpdateRequest evento) {
        return Evento
                .builder()
                .nome(evento.nome())
                .descricao(evento.descricao())
                .local(evento.local())
                .dataInicio(evento.dataInicio())
                .dataFim(evento.dataFim())
                .valorSenha(evento.valorSenha())
                .limiteDuplasPorRodizio(evento.limiteDuplasPorRodizio())
                .status(evento.status())
                .build();
    }
}

