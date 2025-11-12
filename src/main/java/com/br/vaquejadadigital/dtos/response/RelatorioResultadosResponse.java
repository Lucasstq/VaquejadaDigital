package com.br.vaquejadadigital.dtos.response;

import com.br.vaquejadadigital.entities.Categoria;
import com.br.vaquejadadigital.entities.enums.TipoCategoria;
import com.br.vaquejadadigital.entities.enums.TipoResultado;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record RelatorioResultadosResponse(
        String tituloRelatorio,
        Long eventoId,
        String nomeEvento,
        LocalDateTime dataGeracao,
        TipoCategoria categoria,
        Integer totalResultados,
        ResumoResultados resumo,
        List<ItemResultado> resultados
) {
    @Builder
    public record ResumoResultados(
            Integer totalValeBoi,
            Integer totalZeros,
            Integer totalRetornos,
            Integer totalFaltas,
            Integer totalDesclassificados,
            Double percentualSucesso
    ) {}
    @Builder
    public record ItemResultado(
            Long id,
            Integer posicao,
            String puxador,
            String esteireiro,
            String dupla,
            Categoria categoria,
            Integer rodizio,
            Integer ordemNaFila,
            TipoResultado resultado,
            String observacoes,
            LocalDateTime dataRegistro
    ) {}
}
