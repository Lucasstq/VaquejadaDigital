package com.br.vaquejadadigital.controller;

import com.br.vaquejadadigital.dtos.request.CorredorRequest;
import com.br.vaquejadadigital.dtos.response.CorredorResponse;
import com.br.vaquejadadigital.entities.Corredor;
import com.br.vaquejadadigital.mapper.CorredorMapper;
import com.br.vaquejadadigital.service.CorredorService;
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
@RequestMapping("/api/corredores")
@RequiredArgsConstructor
@Slf4j
public class CorredorController {

    private final CorredorService corredorService;

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<CorredorResponse> criar(@PathVariable Long usuarioId, @Valid @RequestBody CorredorRequest request) {
        log.info("Criando perfil de corredor para usuário {}", usuarioId);

        Corredor corredor = CorredorMapper.toEntity(request);
        Corredor savedCorredor = corredorService.criar(usuarioId, corredor);

        return ResponseEntity.status(HttpStatus.CREATED).body(CorredorMapper.toResponse(savedCorredor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CorredorResponse> buscarPorId(@PathVariable Long id) {
        log.info("Buscando corredor por ID: {}", id);
        Corredor corredor = corredorService.buscarPorId(id);
        return ResponseEntity.ok(CorredorMapper.toResponse(corredor));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CorredorResponse> buscarPorUsuarioId(@PathVariable Long usuarioId) {
        log.info("Buscando corredor por usuário ID: {}", usuarioId);
        Corredor corredor = corredorService.buscarPorUsuarioId(usuarioId);
        return ResponseEntity.ok(CorredorMapper.toResponse(corredor));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CorredorResponse>> listarTodos() {
        log.info("Listando todos os corredores");
        List<CorredorResponse> response = corredorService.listarTodos().stream()
                .map(CorredorMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CorredorResponse> atualizar(@PathVariable Long id, @Valid @RequestBody CorredorRequest request) {
        log.info("Atualizando corredor ID: {}", id);

        Corredor corredorAtualizado = CorredorMapper.toEntity(request);
        Corredor corredor = corredorService.atualizar(id, corredorAtualizado);

        return ResponseEntity.ok(CorredorMapper.toResponse(corredor));
    }
}
