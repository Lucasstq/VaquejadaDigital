package com.br.vaquejadadigital.controller;

import com.br.vaquejadadigital.dtos.request.HistoricoCorredorRequest;
import com.br.vaquejadadigital.dtos.response.EstatisticasCorredorResponse;
import com.br.vaquejadadigital.dtos.response.HistoricoCorredorResponse;
import com.br.vaquejadadigital.entities.Corredor;
import com.br.vaquejadadigital.entities.Evento;
import com.br.vaquejadadigital.entities.HistoricoCorredor;
import com.br.vaquejadadigital.entities.enums.TipoCategoria;
import com.br.vaquejadadigital.mapper.HistoricoCorredorMapper;
import com.br.vaquejadadigital.service.CorredorService;
import com.br.vaquejadadigital.service.EventoService;
import com.br.vaquejadadigital.service.HistoricoCorredorService;
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
@RequestMapping("/api/historico")
@RequiredArgsConstructor
@Slf4j
public class HistoricoCorredorController {

    private final HistoricoCorredorService historicoService;
    private final CorredorService corredorService;
    private final EventoService eventoService;

    //Cria ou atualiza histórico de um corredor
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<HistoricoCorredorResponse> criarOuAtualizar(@Valid @RequestBody HistoricoCorredorRequest request) {
        log.info("Criando/atualizando histórico para corredor {} no evento {}",
                request.corredorId(), request.eventoId());

        Corredor corredor = corredorService.buscarPorId(request.corredorId());
        Evento evento = eventoService.buscarPorId(request.eventoId());

        HistoricoCorredor historico = HistoricoCorredorMapper.toEntity(request, corredor, evento);
        HistoricoCorredor saved = historicoService.criarOuAtualizar(historico);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(HistoricoCorredorMapper.toResponse(saved));
    }

    //Busca histórico de um corredor em um evento específico
    @GetMapping("/corredor/{corredorId}/evento/{eventoId}")
    public ResponseEntity<HistoricoCorredorResponse> buscarPorCorredorEvento(
            @PathVariable Long corredorId,
            @PathVariable Long eventoId) {
        log.info("Buscando histórico do corredor {} no evento {}", corredorId, eventoId);

        HistoricoCorredor historico = historicoService.buscarPorCorredorEvento(corredorId, eventoId);

        return ResponseEntity.ok(HistoricoCorredorMapper.toResponse(historico));
    }

   //Lista todo o histórico de um corredor
    @GetMapping("/corredor/{corredorId}")
    public ResponseEntity<List<HistoricoCorredorResponse>> listarPorCorredor(@PathVariable Long corredorId) {
        log.info("Listando histórico completo do corredor {}", corredorId);

        List<HistoricoCorredorResponse> historicos = historicoService.listarPorCorredor(corredorId)
                .stream()
                .map(HistoricoCorredorMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(historicos);
    }

    //Lista histórico de um corredor em uma categoria específica
    @GetMapping("/corredor/{corredorId}/categoria/{categoria}")
    public ResponseEntity<List<HistoricoCorredorResponse>> listarPorCorredorCategoria(
            @PathVariable Long corredorId,
            @PathVariable TipoCategoria categoria) {
        log.info("Listando histórico do corredor {} na categoria {}", corredorId, categoria);

        List<HistoricoCorredorResponse> historicos = historicoService
                .listarPorCorredorCategoria(corredorId, categoria)
                .stream()
                .map(HistoricoCorredorMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(historicos);
    }

    //Lista todos os históricos de um evento (ranking)
    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<HistoricoCorredorResponse>> listarPorEvento(@PathVariable Long eventoId) {
        log.info("Listando históricos do evento {}", eventoId);

        List<HistoricoCorredorResponse> historicos = historicoService.listarPorEvento(eventoId)
                .stream()
                .map(HistoricoCorredorMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(historicos);
    }

    //Calcula estatísticas gerais de um corredor
    @GetMapping("/corredor/{corredorId}/estatisticas")
    public ResponseEntity<EstatisticasCorredorResponse> calcularEstatisticas(@PathVariable Long corredorId) {
        log.info("Calculando estatísticas gerais do corredor {}", corredorId);

        EstatisticasCorredorResponse estatisticas = historicoService.calcularEstatisticas(corredorId);

        return ResponseEntity.ok(estatisticas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoricoCorredorResponse> buscarPorId(@PathVariable Long id) {
        log.info("Buscando histórico ID: {}", id);

        HistoricoCorredor historico = historicoService.buscarPorId(id);

        return ResponseEntity.ok(HistoricoCorredorMapper.toResponse(historico));
    }

    //Atualiza posição final de um corredor no evento
    @PatchMapping("/corredor/{corredorId}/evento/{eventoId}/posicao")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<HistoricoCorredorResponse> atualizarPosicaoFinal(
            @PathVariable Long corredorId,
            @PathVariable Long eventoId,
            @RequestParam Integer posicao,
            @RequestParam(required = false) String colocacao) {
        log.info("Atualizando posição final do corredor {} no evento {}: {}", corredorId, eventoId, posicao);

        HistoricoCorredor historico = historicoService.atualizarPosicaoFinal(corredorId, eventoId, posicao, colocacao);

        return ResponseEntity.ok(HistoricoCorredorMapper.toResponse(historico));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Deletando histórico ID: {}", id);

        historicoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}