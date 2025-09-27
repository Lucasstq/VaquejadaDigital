package br.com.vaquejada_digital.VaquejadaDigital.controller;

import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.EventoResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.EventoRequest;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.EventoUpdateStatusRequest;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Evento;
import br.com.vaquejada_digital.VaquejadaDigital.mapper.EventoMapper;
import br.com.vaquejada_digital.VaquejadaDigital.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vaquejada-digital/evento")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventoResponse> createEvent(@Valid @RequestBody EventoRequest request) {
        Evento evento = EventoMapper.toEvent(request);
        Evento createdEvent = eventoService.createEvent(evento, request.juizesIds(), request.locutoresIds());

        return ResponseEntity.status(HttpStatus.CREATED).body(EventoMapper.toEventResponse(createdEvent));
    }


    @PatchMapping("/{eventoId}/alterar-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventoResponse> alterarStatus(@PathVariable Long eventoId, @RequestBody EventoUpdateStatusRequest request) {
        Evento eventoAtualizado = eventoService.updateStatus(eventoId, request.status());
        return ResponseEntity.ok(EventoMapper.toEventResponse(eventoAtualizado));
    }


    @GetMapping("/todos-eventos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EventoResponse>> findAll() {
        return ResponseEntity.ok(eventoService.findAll()
                .stream()
                .map(EventoMapper::toEventResponse)
                .toList());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventoResponse> updateEvent(@PathVariable Long id, @Valid @RequestBody EventoRequest event) {
        Evento updatedEvent = eventoService.updateEvent(id, EventoMapper.toEvent(event));
        return ResponseEntity.ok(EventoMapper.toEventResponse(updatedEvent));
    }


    @GetMapping
    public ResponseEntity<List<EventoResponse>> findStatusEventos() {
        return ResponseEntity.ok(eventoService.findByStatus()
                .stream()
                .map(EventoMapper::toEventResponse)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> findByIdEventos(@PathVariable Long id) {
        Evento byIdPublico = eventoService.findById(id);
        return ResponseEntity.ok(EventoMapper.toEventResponse(byIdPublico));
    }

}

