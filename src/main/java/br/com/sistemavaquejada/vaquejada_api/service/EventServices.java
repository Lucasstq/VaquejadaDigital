package br.com.sistemavaquejada.vaquejada_api.service;

import br.com.sistemavaquejada.vaquejada_api.entity.Event;
import br.com.sistemavaquejada.vaquejada_api.entity.Enumns.Status;
import br.com.sistemavaquejada.vaquejada_api.exception.EventNotFoundException;
import br.com.sistemavaquejada.vaquejada_api.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServices {
    private final EventRepository eventRepository;

    public Event createEvent(Event event) {
        event.setStatus(Status.ATIVO);
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updateEvent) {
        Event updatedEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado."));

        updatedEvent.setNome(updateEvent.getNome());
        updatedEvent.setDescricao(updateEvent.getDescricao());
        updatedEvent.setLocal(updateEvent.getLocal());
        updatedEvent.setPrecoBaseSenha(updateEvent.getPrecoBaseSenha());
        updatedEvent.setQuantidadeTotalDeSenha(updateEvent.getQuantidadeTotalDeSenha());
        updatedEvent.setImagensVideos(updateEvent.getImagensVideos());
        updatedEvent.setStatus(updateEvent.getStatus());
        eventRepository.save(updatedEvent);
        return updatedEvent;


    }


    public List<Event> findByStatus() {
        return eventRepository.findByStatus(Status.ATIVO);
    }

}
