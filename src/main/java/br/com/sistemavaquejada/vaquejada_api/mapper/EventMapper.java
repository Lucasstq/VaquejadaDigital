package br.com.sistemavaquejada.vaquejada_api.mapper;

import br.com.sistemavaquejada.vaquejada_api.controller.request.EventRequest;
import br.com.sistemavaquejada.vaquejada_api.controller.response.EventResponse;
import br.com.sistemavaquejada.vaquejada_api.entity.Event;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EventMapper {

    public static Event toEvent(EventRequest eventRequest) {
        return Event
                .builder()
                .nome(eventRequest.nome())
                .local(eventRequest.local())
                .descricao(eventRequest.descricao())
                .precoBaseSenha(eventRequest.precoBaseSenha())
                .quantidadeTotalDeSenha(eventRequest.quantidadeTotalDeSenha())
                .imagensVideos(eventRequest.imagensVideos())
                .status(eventRequest.status())
                .build();
    }

    public static EventResponse toEventResponse(Event event) {
        return EventResponse
                .builder()
                .id(event.getId())
                .nome(event.getNome())
                .local(event.getLocal())
                .descricao(event.getDescricao())
                .precoBaseSenha(event.getPrecoBaseSenha())
                .dataInicio(event.getDataInicio())
                .dataFim(event.getDataFim())
                .imagensVideos(event.getImagensVideos())
                .quantidadeTotalDeSenha(event.getQuantidadeTotalDeSenha())
                .status(event.getStatus())
                .build();
    }
}
