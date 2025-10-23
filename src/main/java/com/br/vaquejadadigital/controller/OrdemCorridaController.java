package com.br.vaquejadadigital.controller;

import com.br.vaquejadadigital.dtos.request.OrdemCorridaRequest;
import com.br.vaquejadadigital.dtos.response.OrdemCorridaResponse;
import com.br.vaquejadadigital.entities.Dupla;
import com.br.vaquejadadigital.entities.OrdemCorrida;
import com.br.vaquejadadigital.entities.Rodizio;
import com.br.vaquejadadigital.mapper.OrdemCorridaMapper;
import com.br.vaquejadadigital.service.DuplaService;
import com.br.vaquejadadigital.service.OrdemCorridaService;
import com.br.vaquejadadigital.service.RodizioService;
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
@RequestMapping("/api/ordem-corrida")
@Slf4j
@RequiredArgsConstructor
public class OrdemCorridaController {

    private final OrdemCorridaService ordemCorridaService;
    private final RodizioService rodizioService;
    private final DuplaService duplaService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JUIZ', 'LOCUTOR')")
    public ResponseEntity<OrdemCorridaResponse> adicionar(@Valid @RequestBody OrdemCorridaRequest request) {
        log.info("Adicionando dupla {} na posição {} do rodízio {}",
                request.duplaId(), request.posicao(), request.rodizioId());

        Rodizio rodizio = rodizioService.buscarPorId(request.rodizioId());
        Dupla dupla = duplaService.buscarPorId(request.duplaId());

        OrdemCorrida ordem = OrdemCorrida.builder()
                .rodizio(rodizio)
                .dupla(dupla)
                .posicao(request.posicao())
                .build();

        OrdemCorrida savedOrdem = ordemCorridaService.adicionar(ordem);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrdemCorridaMapper.toResponse(savedOrdem));
    }

    @GetMapping("/rodizio/{rodizioId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JUIZ', 'LOCUTOR', 'CORREDOR')")
    public ResponseEntity<List<OrdemCorridaResponse>> listarPorRodizio(@PathVariable Long rodizioId) {
        log.info("Listando ordem de corrida do rodízio {}", rodizioId);
        List<OrdemCorridaResponse> response = ordemCorridaService.listarPorRodizio(rodizioId).stream()
                .map(OrdemCorridaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/chamar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JUIZ', 'LOCUTOR')")
    public ResponseEntity<OrdemCorridaResponse> chamar(@PathVariable Long id) {
        log.info("Chamando dupla da ordem de corrida ID: {}", id);
        OrdemCorrida ordem = ordemCorridaService.chamarDupla(id);
        return ResponseEntity.ok(OrdemCorridaMapper.toResponse(ordem));
    }

    @PutMapping("/{id}/correndo")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JUIZ', 'LOCUTOR')")
    public ResponseEntity<OrdemCorridaResponse> marcarComoCorrendo(@PathVariable Long id) {
        log.info("Marcando dupla como correndo - Ordem ID: {}", id);
        OrdemCorrida ordem = ordemCorridaService.marcarComoCorrendo(id);
        return ResponseEntity.ok(OrdemCorridaMapper.toResponse(ordem));
    }

    @PutMapping("/{id}/correu")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JUIZ', 'LOCUTOR')")
    public ResponseEntity<OrdemCorridaResponse> marcarComoCorreu(@PathVariable Long id) {
        log.info("Marcando dupla como correu - Ordem ID: {}", id);
        OrdemCorrida ordem = ordemCorridaService.marcarComoCorreu(id);
        return ResponseEntity.ok(OrdemCorridaMapper.toResponse(ordem));
    }

    @PutMapping("/{id}/falta")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JUIZ', 'LOCUTOR')")
    public ResponseEntity<OrdemCorridaResponse> marcarComoFalta(@PathVariable Long id) {
        log.info("Marcando dupla como falta - Ordem ID: {}", id);
        OrdemCorrida ordem = ordemCorridaService.marcarComoFalta(id);
        return ResponseEntity.ok(OrdemCorridaMapper.toResponse(ordem));
    }

}
