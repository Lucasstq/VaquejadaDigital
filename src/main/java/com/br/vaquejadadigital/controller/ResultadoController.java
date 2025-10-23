package com.br.vaquejadadigital.controller;

import com.br.vaquejadadigital.dtos.request.ResultadoRequest;
import com.br.vaquejadadigital.dtos.response.ResultadoResponse;
import com.br.vaquejadadigital.entities.Resultado;
import com.br.vaquejadadigital.mapper.ResultadoMapper;
import com.br.vaquejadadigital.service.ResultadoService;
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
@RequestMapping("/api/resultados")
@RequiredArgsConstructor
@Slf4j
public class ResultadoController {

    private final ResultadoService resultadoService;
    private final ResultadoMapper resultadoMapper;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JUIZ')")
    public ResponseEntity<ResultadoResponse> registrar(@Valid @RequestBody ResultadoRequest request) {
        log.info("Iniciando registro de resultado");
        Resultado savedResultado = resultadoService.registrar(resultadoMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(resultadoMapper.toResponse(savedResultado));
    }

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<ResultadoResponse>> listarPorEvento(@PathVariable Long eventoId) {
        log.info("Iniciando listar resultados por evento");
        List<ResultadoResponse> response = resultadoService.listarPorEvento(eventoId).stream()
                .map(resultadoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dupla/{duplaId}")
    public ResponseEntity<List<ResultadoResponse>> listarPorDupla(@PathVariable Long duplaId) {
        log.info("Iniciando listar resultados por dupla");
        List<Resultado> resultados = resultadoService.listarPorDupla(duplaId);
        List<ResultadoResponse> response = resultados.stream()
                .map(resultadoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ordem-corrida/{ordemCorridaId}")
    public ResponseEntity<ResultadoResponse> buscarPorOrdemCorrida(@PathVariable Long ordemCorridaId) {
        log.info("Buscar resultado por ordem de corrida");
        Resultado resultado = resultadoService.buscarPorOrdemCorrida(ordemCorridaId);
        ResultadoResponse response = resultadoMapper.toResponse(resultado);
        return ResponseEntity.ok(response);
    }
}
