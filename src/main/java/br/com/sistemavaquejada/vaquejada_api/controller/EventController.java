package br.com.sistemavaquejada.vaquejada_api.controller;

import br.com.sistemavaquejada.vaquejada_api.mapper.EventMapper;
import br.com.sistemavaquejada.vaquejada_api.controller.request.EventRequest;
import br.com.sistemavaquejada.vaquejada_api.controller.response.EventResponse;
import br.com.sistemavaquejada.vaquejada_api.entity.Event;
import br.com.sistemavaquejada.vaquejada_api.service.EventServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/vaquejada/evento")
public class EventController {

    private final EventServices eventServices;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest event) {
        Event createdEvent = eventServices.createEvent(EventMapper.toEvent(event));
        return ResponseEntity.status(HttpStatus.CREATED).body(EventMapper.toEventResponse(createdEvent));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id, @RequestBody EventRequest eventRequest) {
        Event event = eventServices.updateEvent(id, EventMapper.toEvent(eventRequest));
        return ResponseEntity.ok(EventMapper.toEventResponse(event));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> findAllEvents() {
        return ResponseEntity.ok(eventServices.findByStatus()
                .stream()
                .map(EventMapper::toEventResponse)
                .toList());
    }

}
