package br.com.vaquejada_digital.VaquejadaDigital.controller;

import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.OrdemCorridaResponse;
import br.com.vaquejada_digital.VaquejadaDigital.entity.OrdemCorrida;
import br.com.vaquejada_digital.VaquejadaDigital.mapper.OrdemCorridaMapper;
import br.com.vaquejada_digital.VaquejadaDigital.service.OrdemCorridaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vaquejada-digital/locutor")
@RequiredArgsConstructor
@PreAuthorize("hasRole('LOCUTOR')")
public class LocutorController {

    private final OrdemCorridaService ordemCorridaService;

    @GetMapping("/rodizio/{rodizioId}/ordem-atual")
    public ResponseEntity<List<OrdemCorridaResponse>> verOrdemAtual(
            @PathVariable Long rodizioId) {

        List<OrdemCorrida> ordens = ordemCorridaService.buscarPorRodizio(rodizioId);

        return ResponseEntity.ok(ordens.stream()
                .map(OrdemCorridaMapper::toResponse)
                .toList());
    }

    @PostMapping("/chamar-proxima/{rodizioId}")
    public ResponseEntity<OrdemCorridaResponse> chamarProxima(
            @PathVariable Long rodizioId) {

        OrdemCorrida chamada = ordemCorridaService.chamarProxima(rodizioId);

        return ResponseEntity.ok(OrdemCorridaMapper.toResponse(chamada));
    }

    @PutMapping("/marcar-falta/{ordemCorridaId}")
    public ResponseEntity<OrdemCorridaResponse> marcarFalta(
            @PathVariable Long ordemCorridaId) {

        OrdemCorrida ordem = ordemCorridaService.marcarFalta(ordemCorridaId);

        return ResponseEntity.ok(OrdemCorridaMapper.toResponse(ordem));
    }
}
