package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.dtos.response.RelatorioResultadosResponse;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CsvExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    //Exporta relatório em formato CSV
    public byte[] exportarParaCsv(RelatorioResultadosResponse relatorio) {
        log.info("Exportando relatório para CSV: {}", relatorio.tituloRelatorio());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter osw = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
             CSVWriter writer = new CSVWriter(osw)) {

            // Adicionar BOM para Excel reconhecer UTF-8
            baos.write(0xEF);
            baos.write(0xBB);
            baos.write(0xBF);

            List<String[]> dados = new ArrayList<>();

            // Título e informações
            dados.add(new String[]{relatorio.tituloRelatorio()});
            dados.add(new String[]{"Evento: " + relatorio.nomeEvento()});
            dados.add(new String[]{"Data de Geração: " + relatorio.dataGeracao().format(DATE_FORMATTER)});

            if (relatorio.categoria() != null) {
                dados.add(new String[]{"Categoria: " + relatorio.categoria().getDescricao()});
            }

            dados.add(new String[]{""});

            // Resumo
            dados.add(new String[]{"RESUMO ESTATÍSTICO"});
            dados.add(new String[]{"Valeu o Boi", String.valueOf(relatorio.resumo().totalValeBoi())});
            dados.add(new String[]{"Zero", String.valueOf(relatorio.resumo().totalZeros())});
            dados.add(new String[]{"Retorno", String.valueOf(relatorio.resumo().totalRetornos())});
            dados.add(new String[]{"Falta", String.valueOf(relatorio.resumo().totalFaltas())});
            dados.add(new String[]{"Desclassificado", String.valueOf(relatorio.resumo().totalDesclassificados())});
            dados.add(new String[]{"Taxa de Sucesso", String.format("%.2f%%", relatorio.resumo().percentualSucesso())});

            dados.add(new String[]{""});
            dados.add(new String[]{""});

            // Header da tabela de resultados
            dados.add(new String[]{
                    "#",
                    "Puxador",
                    "Esteireiro",
                    "Dupla",
                    "Categoria",
                    "Rodízio",
                    "Ordem na Fila",
                    "Resultado",
                    "Observações",
                    "Data Registro"
            });

            // Dados dos resultados
            int contador = 1;
            for (var item : relatorio.resultados()) {
                dados.add(new String[]{
                        String.valueOf(contador++),
                        item.puxador(),
                        item.esteireiro(),
                        item.dupla(),
                        item.categoria().getDescricao(),
                        String.valueOf(item.rodizio()),
                        String.valueOf(item.ordemNaFila()),
                        item.resultado().getDescricao(),
                        item.observacoes() != null ? item.observacoes() : "",
                        item.dataRegistro().format(DATE_FORMATTER)
                });
            }

            // Escrever todos os dados
            writer.writeAll(dados);
            writer.flush();

            log.info("CSV gerado com sucesso. Tamanho: {} bytes", baos.size());
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Erro ao gerar CSV", e);
            throw new RuntimeException("Erro ao gerar CSV: " + e.getMessage(), e);
        }
    }
}
