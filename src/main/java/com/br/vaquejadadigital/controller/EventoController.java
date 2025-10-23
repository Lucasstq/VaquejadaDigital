package com.br.vaquejadadigital.controller;

import com.br.vaquejadadigital.dtos.request.EventoRequest;
import com.br.vaquejadadigital.dtos.request.EventoUpdateRequest;
import com.br.vaquejadadigital.dtos.response.EventoResponse;
import com.br.vaquejadadigital.dtos.response.EventoResumoResponse;
import com.br.vaquejadadigital.entities.Evento;
import com.br.vaquejadadigital.mapper.EventoMapper;
import com.br.vaquejadadigital.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
@Slf4j
public class EventoController {

    private final EventoService eventoService;
    private final EventoMapper eventoMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EventoResponse> criarEvento(@Valid @RequestBody EventoRequest request) {
        log.info("Criando evento");
        Evento savedEvento = eventoService.criarEvento(eventoMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoMapper.toResponse(savedEvento));
    }


    @GetMapping
    public ResponseEntity<List<EventoResponse>> listarTodos() {
        log.info("Listando todos eventos");
        List<EventoResponse> response = eventoService.listarTodos().stream()
                .map(eventoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<EventoResumoResponse>> listarEventosAtivos() {
        log.info("Listando eventos ativos");
        List<EventoResumoResponse> response = eventoService.listarEventosAtivos().stream()
                .map(eventoMapper::toResumo)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> buscarPorId(@PathVariable Long id) {
        log.info("Buscando evento por id");
        Evento evento = eventoService.buscarPorId(id);
        return ResponseEntity.ok(eventoMapper.toResponse(evento));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EventoResponse> atualizarEvento(@PathVariable Long id, @Valid @RequestBody EventoUpdateRequest request) {
        log.info("Atualizando evento");
        Evento eventoAtualizado = eventoMapper.toEntityFromUpdate(request);
        Evento evento = eventoService.atualizarEvento(id, eventoAtualizado);
        return ResponseEntity.ok(eventoMapper.toResponse(evento));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deletarEvento(@PathVariable Long id) {
        eventoService.deletarEvento(id);
        return ResponseEntity.noContent().build();
    }

}

