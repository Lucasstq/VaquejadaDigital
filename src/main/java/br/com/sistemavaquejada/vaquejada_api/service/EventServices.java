package br.com.sistemavaquejada.vaquejada_api.service;

import br.com.sistemavaquejada.vaquejada_api.entity.Event;
import br.com.sistemavaquejada.vaquejada_api.exception.EventNotFoundException;
import br.com.sistemavaquejada.vaquejada_api.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServices {
    private final EventRepository eventRepository;

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updateEvent) {
        Optional<Event> optEvent = eventRepository.findById(id);
        if (optEvent.isPresent()) {
            Event updatedEvent = optEvent.get();
            updatedEvent.setNome(updateEvent.getNome());
            updatedEvent.setDescricao(updateEvent.getDescricao());
            updatedEvent.setLocal(updateEvent.getLocal());
            updatedEvent.setPrecoBaseSenha(updateEvent.getPrecoBaseSenha());
            updatedEvent.setQuantidadeTotalDeSenha(updateEvent.getQuantidadeTotalDeSenha());
            updatedEvent.setImagensVideos(updateEvent.getImagensVideos());
            updatedEvent.setStatus(updateEvent.getStatus());
            eventRepository.save(updatedEvent);
            return updatedEvent;
        } else {
            throw new EventNotFoundException("Evento não encontrado!");
        }

    }


}
