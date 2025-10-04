package br.com.vaquejada_digital.VaquejadaDigital.controller;

import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.OrdemCorridaResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.RegistrarResultadoRequest;
import br.com.vaquejada_digital.VaquejadaDigital.entity.OrdemCorrida;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import br.com.vaquejada_digital.VaquejadaDigital.mapper.OrdemCorridaMapper;
import br.com.vaquejada_digital.VaquejadaDigital.service.OrdemCorridaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@PreAuthorize("hasRole('JUIZ')")
public class JuizController {

    private final OrdemCorridaService ordemCorridaService;

    @PostMapping("/registrar-resultado")
    public ResponseEntity<OrdemCorridaResponse> registrarResultado(
            @Valid @RequestBody RegistrarResultadoRequest request,
            Authentication authentication) {

        Usuarios juiz = (Usuarios) authentication.getPrincipal();

        OrdemCorrida ordem = ordemCorridaService.registrarResultado(
                request.ordemCorridaId(),
                request.resultado(),
                juiz
        );

        return ResponseEntity.ok(OrdemCorridaMapper.toResponse(ordem));
    }


    @GetMapping("/rodizio/{rodizioId}/ordem-atual")
    public ResponseEntity<List<OrdemCorridaResponse>> verOrdemAtual(@PathVariable Long rodizioId) {

        List<OrdemCorrida> ordens = ordemCorridaService.buscarPorRodizio(rodizioId);

        return ResponseEntity.ok(ordens.stream()
                .map(OrdemCorridaMapper::toResponse)
                .toList());
    }


    @PatchMapping("/observacoes/{ordemCorridaId}")
    public ResponseEntity<OrdemCorridaResponse> atualizarObservacoes(@PathVariable Long ordemCorridaId, @RequestBody String observacoes) {

        OrdemCorrida ordem = ordemCorridaService.atualizarObservacoes(
                ordemCorridaId,
                observacoes
        );

        return ResponseEntity.ok(OrdemCorridaMapper.toResponse(ordem));
    }

}
