package br.com.vaquejada_digital.VaquejadaDigital.controller;

import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.RodizioResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.AtualizarStatusRodizioRequest;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.CriarRodizioAutomaticoRequest;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.CriarRodizioManualRequest;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Rodizio;
import br.com.vaquejada_digital.VaquejadaDigital.mapper.RodizioMapper;
import br.com.vaquejada_digital.VaquejadaDigital.service.RodizioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vaquejada-digital/rodizios")
@RequiredArgsConstructor
public class RodizioController {

    private final RodizioService rodizioService;


    @PostMapping("/automatico")
    public ResponseEntity<List<RodizioResponse>> criarAutomatico(@Valid @RequestBody CriarRodizioAutomaticoRequest request) {
        List<Rodizio> rodizios = rodizioService.criarRodzioAutomatico(
                request.eventoId(),
                request.categoriaId(),
                request.tamanhoRodizio()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(rodizios.stream()
                .map(RodizioMapper::toRodizioResponse)
                .toList());
    }

    @PostMapping("/manual")
    public ResponseEntity<RodizioResponse> criarManual(@Valid @RequestBody CriarRodizioManualRequest request) {
        Rodizio rodizio = rodizioService.criarRodizioManual(request.eventoId(), request.categoriaId(), request.senhaIds());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RodizioMapper.toRodizioResponse(rodizio));
    }

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<RodizioResponse>> listarPorEvento(@PathVariable Long eventoId) {
        List<Rodizio> rodizios = rodizioService.buscarPorEvento(eventoId);

        return ResponseEntity.ok(rodizios.stream()
                .map(RodizioMapper::toRodizioResponse)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RodizioResponse> buscarPorId(@PathVariable Long id) {
        return rodizioService.buscarPorId(id)
                .map(rodizio -> ResponseEntity.ok(RodizioMapper.toRodizioResponse(rodizio)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RodizioResponse> atualizarStatus(@PathVariable Long id, @Valid @RequestBody AtualizarStatusRodizioRequest request) {
        Rodizio rodizio = rodizioService.atualizarStatus(id, request.status());
        return ResponseEntity.ok(RodizioMapper.toRodizioResponse(rodizio));
    }
}
