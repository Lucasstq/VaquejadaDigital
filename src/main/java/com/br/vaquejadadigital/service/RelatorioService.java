package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.dtos.request.RelatorioFiltroRequest;
import com.br.vaquejadadigital.dtos.response.RelatorioResultadosResponse;
import com.br.vaquejadadigital.entities.Evento;
import com.br.vaquejadadigital.entities.Resultado;
import com.br.vaquejadadigital.entities.enums.TipoCategoria;
import com.br.vaquejadadigital.entities.enums.TipoResultado;
import com.br.vaquejadadigital.repositories.ResultadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelatorioService {

    private final ResultadoRepository resultadoRepository;
    private final EventoService eventoService;

    //Gera relatório de resultados com filtros
    @Transactional(readOnly = true)
    public RelatorioResultadosResponse gerarRelatorio(RelatorioFiltroRequest filtro) {
        log.info("Gerando relatório com filtros: {}", filtro);

        // Buscar evento
        Evento evento = eventoService.buscarPorId(filtro.eventoId());

        // Buscar resultados aplicando filtros
        List<Resultado> resultados = buscarResultadosComFiltros(filtro);

        // Montar resumo
        RelatorioResultadosResponse.ResumoResultados resumo = calcularResumo(resultados);

        // Montar itens do relatório
        List<RelatorioResultadosResponse.ItemResultado> itens = montarItensRelatorio(resultados);

        // Montar título
        String titulo = montarTituloRelatorio(evento, filtro);

        return RelatorioResultadosResponse.builder()
                .tituloRelatorio(titulo)
                .eventoId(evento.getId())
                .nomeEvento(evento.getNome())
                .dataGeracao(LocalDateTime.now())
                .categoria(filtro.categoria())
                .totalResultados(resultados.size())
                .resumo(resumo)
                .resultados(itens)
                .build();
    }

    //Busca resultados aplicando os filtros
    private List<Resultado> buscarResultadosComFiltros(RelatorioFiltroRequest filtro) {
        List<Resultado> resultados;

        resultados = resultadoRepository.findByEventoId(filtro.eventoId());

        // Filtro por categoria
        if (filtro.categoria() != null) {
            TipoCategoria categoria = filtro.categoria();
            resultados = resultados.stream()
                    .filter(r -> r.getOrdemCorrida().getRodizio().getCategoria().getNome() == categoria)
                    .collect(Collectors.toList());
        }

        // Filtro por corredor (puxador ou esteireiro)
        if (filtro.corredorId() != null) {
            Long corredorId = filtro.corredorId();
            resultados = resultados.stream()
                    .filter(r ->
                            r.getOrdemCorrida().getDupla().getPuxador().getId().equals(corredorId) ||
                                    r.getOrdemCorrida().getDupla().getEsteireiro().getId().equals(corredorId)
                    )
                    .collect(Collectors.toList());
        }

        // Filtro por rodízio
        if (filtro.rodizioId() != null) {
            Long rodizioId = filtro.rodizioId();
            resultados = resultados.stream()
                    .filter(r -> r.getOrdemCorrida().getRodizio().getId().equals(rodizioId))
                    .collect(Collectors.toList());
        }

        // Filtro por tipo de resultado
        if (filtro.tipoResultado() != null) {
            TipoResultado tipo = filtro.tipoResultado();
            resultados = resultados.stream()
                    .filter(r -> r.getTipoResultado() == tipo)
                    .collect(Collectors.toList());
        }

        return resultados;
    }

    //Calcula resumo estatístico dos resultados
    private RelatorioResultadosResponse.ResumoResultados calcularResumo(List<Resultado> resultados) {
        int totalValeBoi = (int) resultados.stream()
                .filter(r -> r.getTipoResultado() == TipoResultado.VALEU_O_BOI)
                .count();

        int totalZeros = (int) resultados.stream()
                .filter(r -> r.getTipoResultado() == TipoResultado.ZERO)
                .count();

        int totalRetornos = (int) resultados.stream()
                .filter(r -> r.getTipoResultado() == TipoResultado.RETORNO)
                .count();

        int totalFaltas = (int) resultados.stream()
                .filter(r -> r.getTipoResultado() == TipoResultado.FALTA)
                .count();

        int totalDesclassificados = (int) resultados.stream()
                .filter(r -> r.getTipoResultado() == TipoResultado.DESCLASSIFICADO)
                .count();

        double percentualSucesso = resultados.isEmpty() ? 0.0 :
                (totalValeBoi * 100.0) / resultados.size();

        return RelatorioResultadosResponse.ResumoResultados.builder()
                .totalValeBoi(totalValeBoi)
                .totalZeros(totalZeros)
                .totalRetornos(totalRetornos)
                .totalFaltas(totalFaltas)
                .totalDesclassificados(totalDesclassificados)
                .percentualSucesso(Math.round(percentualSucesso * 100.0) / 100.0)
                .build();
    }

    //Monta lista de itens do relatório
    private List<RelatorioResultadosResponse.ItemResultado> montarItensRelatorio(List<Resultado> resultados) {
        return resultados.stream()
                .map(resultado -> {
                    var ordem = resultado.getOrdemCorrida();
                    var dupla = ordem.getDupla();
                    var puxador = dupla.getPuxador();
                    var esteireiro = dupla.getEsteireiro();

                    return RelatorioResultadosResponse.ItemResultado.builder()
                            .id(resultado.getId())
                            .posicao(ordem.getPosicao())
                            .puxador(puxador.getUsuario().getNome())
                            .esteireiro(esteireiro.getUsuario().getNome())
                            .dupla(puxador.getApelido() + " / " + esteireiro.getApelido())
                            .categoria(ordem.getRodizio().getCategoria())
                            .rodizio(ordem.getRodizio().getNumeroRodizio())
                            .ordemNaFila(ordem.getPosicao())
                            .resultado(resultado.getTipoResultado())
                            .observacoes(resultado.getObservacao())
                            .dataRegistro(resultado.getDataRegistro())
                            .build();
                })
                .collect(Collectors.toList());
    }

    //Monta título do relatório baseado nos filtros
    private String montarTituloRelatorio(Evento evento, RelatorioFiltroRequest filtro) {
        StringBuilder titulo = new StringBuilder("Relatório de Resultados - " + evento.getNome());

        if (filtro.categoria() != null) {
            titulo.append(" - Categoria: ").append(filtro.categoria().getDescricao());
        }

        if (filtro.tipoResultado() != null) {
            titulo.append(" - Resultado: ").append(filtro.tipoResultado().getDescricao());
        }

        if (filtro.rodizioId() != null) {
            titulo.append(" - Rodízio: ").append(filtro.rodizioId());
        }

        return titulo.toString();
    }
}
