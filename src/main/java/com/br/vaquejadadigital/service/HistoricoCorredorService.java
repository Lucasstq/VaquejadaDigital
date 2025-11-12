package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.dtos.response.EstatisticasCorredorResponse;
import com.br.vaquejadadigital.entities.Corredor;
import com.br.vaquejadadigital.entities.Evento;
import com.br.vaquejadadigital.entities.HistoricoCorredor;
import com.br.vaquejadadigital.entities.enums.TipoCategoria;
import com.br.vaquejadadigital.exception.ResourceNotFoundException;
import com.br.vaquejadadigital.repositories.HistoricoCorredorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoricoCorredorService {

    private final HistoricoCorredorRepository historicoRepository;
    private final CorredorService corredorService;
    private final EventoService eventoService;

    // Cria ou atualiza histórico de um corredor em um evento
    @Transactional
    public HistoricoCorredor criarOuAtualizar(HistoricoCorredor historico) {
        log.info("Criando/atualizando histórico para corredor {} no evento {}",
                historico.getCorredor().getId(), historico.getEvento().getId());

        // Verifica se já existe histórico
        historicoRepository.findByCorredorIdAndEventoId(
                historico.getCorredor().getId(),
                historico.getEvento().getId()
        ).ifPresent(existente -> historico.setId(existente.getId()));

        historico.calcularTaxaSucesso();
        return historicoRepository.save(historico);
    }

    //Busca histórico de um corredor em um evento específico
    @Transactional(readOnly = true)
    public HistoricoCorredor buscarPorCorredorEvento(Long corredorId, Long eventoId) {
        log.info("Buscando histórico do corredor {} no evento {}", corredorId, eventoId);

        return historicoRepository.findByCorredorIdAndEventoId(corredorId, eventoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Histórico não encontrado para corredor " + corredorId + " no evento " + eventoId));
    }

    //Lista todo o histórico de um corredor
    @Transactional(readOnly = true)
    public List<HistoricoCorredor> listarPorCorredor(Long corredorId) {
        log.info("Listando histórico completo do corredor {}", corredorId);

        // Verifica se corredor existe
        corredorService.buscarPorId(corredorId);

        return historicoRepository.findByCorredorIdOrderByDataCriacaoDesc(corredorId);
    }

    //Lista histórico de um corredor em uma categoria específica
    @Transactional(readOnly = true)
    public List<HistoricoCorredor> listarPorCorredorCategoria(Long corredorId, TipoCategoria categoria) {
        log.info("Listando histórico do corredor {} na categoria {}", corredorId, categoria);

        // Verifica se corredor existe
        corredorService.buscarPorId(corredorId);

        return historicoRepository.findByCorredorIdAndCategoriaOrderByDataCriacaoDesc(corredorId, categoria);
    }

    // Lista todos os históricos de um evento
    @Transactional(readOnly = true)
    public List<HistoricoCorredor> listarPorEvento(Long eventoId) {
        log.info("Listando históricos do evento {}", eventoId);

        // Verifica se evento existe
        eventoService.buscarPorId(eventoId);

        return historicoRepository.findByEventoIdOrderByPosicaoFinalAsc(eventoId);
    }

    //Calcula estatísticas gerais de um corredor
    @Transactional(readOnly = true)
    public EstatisticasCorredorResponse calcularEstatisticas(Long corredorId) {
        log.info("Calculando estatísticas gerais do corredor {}", corredorId);

        Corredor corredor = corredorService.buscarPorId(corredorId);

        // Busca estatísticas agregadas
        Object[] stats = historicoRepository.calcularEstatisticasGerais(corredorId);

        Integer totalCorridas = stats[0] != null ? ((Number) stats[0]).intValue() : 0;
        Integer totalValeBoi = stats[1] != null ? ((Number) stats[1]).intValue() : 0;
        Integer totalZeros = stats[2] != null ? ((Number) stats[2]).intValue() : 0;
        Integer totalRetornos = stats[3] != null ? ((Number) stats[3]).intValue() : 0;
        Integer totalFaltas = stats[4] != null ? ((Number) stats[4]).intValue() : 0;
        Integer totalDesclassificacoes = stats[5] != null ? ((Number) stats[5]).intValue() : 0;
        Integer totalEventos = stats[6] != null ? ((Number) stats[6]).intValue() : 0;

        // Calcula taxa de sucesso geral
        BigDecimal taxaSucessoGeral = BigDecimal.ZERO;
        if (totalCorridas > 0) {
            taxaSucessoGeral = BigDecimal.valueOf(totalValeBoi)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalCorridas), 2, RoundingMode.HALF_UP);
        }

        // Busca melhor posição
        List<HistoricoCorredor> melhoresColocacoes = historicoRepository.findMelhoresColocacoes(corredorId);
        Integer melhorPosicao = null;
        String eventoMelhorPosicao = null;

        if (!melhoresColocacoes.isEmpty()) {
            HistoricoCorredor melhor = melhoresColocacoes.get(0);
            melhorPosicao = melhor.getPosicaoFinal();
            eventoMelhorPosicao = melhor.getEvento().getNome();
        }

        return EstatisticasCorredorResponse.builder()
                .corredorId(corredor.getId())
                .nomeCompleto(corredor.getUsuario().getNome())
                .apelido(corredor.getApelido())
                .totalEventos(totalEventos)
                .totalCorridas(totalCorridas)
                .totalValeBoi(totalValeBoi)
                .totalZeros(totalZeros)
                .totalRetornos(totalRetornos)
                .totalFaltas(totalFaltas)
                .totalDesclassificacoes(totalDesclassificacoes)
                .taxaSucessoGeral(taxaSucessoGeral)
                .melhorPosicao(melhorPosicao)
                .eventoMelhorPosicao(eventoMelhorPosicao)
                .build();
    }

    /**
     * Incrementa estatísticas quando um resultado é registrado
     * Deve ser chamado pelo ResultadoService quando um resultado é criado
     */
    @Transactional
    public void incrementarResultado(Long corredorId, Long eventoId, TipoCategoria categoria, String tipoResultado) {
        log.info("Incrementando resultado {} para corredor {} no evento {}", tipoResultado, corredorId, eventoId);

        Corredor corredor = corredorService.buscarPorId(corredorId);
        Evento evento = eventoService.buscarPorId(eventoId);

        // Busca ou cria histórico
        HistoricoCorredor historico = historicoRepository
                .findByCorredorIdAndEventoId(corredorId, eventoId)
                .orElseGet(() -> HistoricoCorredor.builder()
                        .corredor(corredor)
                        .evento(evento)
                        .categoria(categoria)
                        .totalCorridas(0)
                        .valeBoi(0)
                        .zeros(0)
                        .retornos(0)
                        .faltas(0)
                        .desclassificacoes(0)
                        .pontuacaoTotal(BigDecimal.ZERO)
                        .build());

        historico.incrementarEstatistica(tipoResultado);

        historicoRepository.save(historico);
    }

    //Busca histórico por ID
    @Transactional(readOnly = true)
    public HistoricoCorredor buscarPorId(Long id) {
        return historicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico não encontrado com ID: " + id));
    }

    //Atualiza posição final de um corredor no evento
    @Transactional
    public HistoricoCorredor atualizarPosicaoFinal(Long corredorId, Long eventoId, Integer posicao, String colocacao) {
        log.info("Atualizando posição final do corredor {} no evento {}: {}", corredorId, eventoId, posicao);

        HistoricoCorredor historico = buscarPorCorredorEvento(corredorId, eventoId);
        historico.setPosicaoFinal(posicao);
        historico.setColocacaoGeral(colocacao);

        return historicoRepository.save(historico);
    }

    //deleta um historico
    @Transactional
    public void deletar(Long id) {
        log.info("Deletando histórico ID: {}", id);

        if (!historicoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Histórico não encontrado com ID: " + id);
        }

        historicoRepository.deleteById(id);
    }
}