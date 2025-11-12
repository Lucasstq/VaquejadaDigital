package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.dtos.response.RelatorioResultadosResponse;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class PdfExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    //Exporta relatório em formato PDF
    public byte[] exportarParaPdf(RelatorioResultadosResponse relatorio) {
        log.info("Exportando relatório para PDF: {}", relatorio.tituloRelatorio());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Título
            adicionarTitulo(document, relatorio);

            // Informações do evento
            adicionarInformacoesEvento(document, relatorio);

            // Resumo estatístico
            adicionarResumo(document, relatorio.resumo());

            // Tabela de resultados
            adicionarTabelaResultados(document, relatorio);

            // Rodapé
            adicionarRodape(document);

            document.close();

            log.info("PDF gerado com sucesso. Tamanho: {} bytes", baos.size());
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Erro ao gerar PDF", e);
            throw new RuntimeException("Erro ao gerar PDF: " + e.getMessage(), e);
        }
    }

    private void adicionarTitulo(Document document, RelatorioResultadosResponse relatorio) {
        Paragraph titulo = new Paragraph(relatorio.tituloRelatorio())
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(titulo);
    }

    private void adicionarInformacoesEvento(Document document, RelatorioResultadosResponse relatorio) {
        document.add(new Paragraph("Evento: " + relatorio.nomeEvento()).setBold());
        document.add(new Paragraph("Data de Geração: " +
                relatorio.dataGeracao().format(DATE_FORMATTER)));

        if (relatorio.categoria() != null) {
            document.add(new Paragraph("Categoria: " + relatorio.categoria().getDescricao()));
        }

        document.add(new Paragraph("\n"));
    }

    private void adicionarResumo(Document document, RelatorioResultadosResponse.ResumoResultados resumo) {
        document.add(new Paragraph("RESUMO ESTATÍSTICO").setBold().setFontSize(14));

        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 1}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        // Header
        table.addHeaderCell(createHeaderCell("Resultado"));
        table.addHeaderCell(createHeaderCell("Quantidade"));

        // Dados
        table.addCell("Valeu o Boi");
        table.addCell(String.valueOf(resumo.totalValeBoi()));

        table.addCell("Zero");
        table.addCell(String.valueOf(resumo.totalZeros()));

        table.addCell("Retorno");
        table.addCell(String.valueOf(resumo.totalRetornos()));

        table.addCell("Falta");
        table.addCell(String.valueOf(resumo.totalFaltas()));

        table.addCell("Desclassificado");
        table.addCell(String.valueOf(resumo.totalDesclassificados()));

        table.addCell(createHeaderCell("Taxa de Sucesso"));
        table.addCell(createHeaderCell(String.format("%.2f%%", resumo.percentualSucesso())));

        document.add(table);
    }

    private void adicionarTabelaResultados(Document document, RelatorioResultadosResponse relatorio) {
        document.add(new Paragraph("RESULTADOS DETALHADOS").setBold().setFontSize(14));

        Table table = new Table(UnitValue.createPercentArray(
                new float[]{1, 3, 3, 2, 1, 1, 2}))
                .useAllAvailableWidth();

        // Header
        table.addHeaderCell(createHeaderCell("#"));
        table.addHeaderCell(createHeaderCell("Puxador"));
        table.addHeaderCell(createHeaderCell("Esteireiro"));
        table.addHeaderCell(createHeaderCell("Categoria"));
        table.addHeaderCell(createHeaderCell("Rodízio"));
        table.addHeaderCell(createHeaderCell("Ordem"));
        table.addHeaderCell(createHeaderCell("Resultado"));

        // Dados
        int contador = 1;
        for (var item : relatorio.resultados()) {
            table.addCell(String.valueOf(contador++));
            table.addCell(item.puxador());
            table.addCell(item.esteireiro());
            table.addCell(item.categoria().getDescricao());
            table.addCell(String.valueOf(item.rodizio()));
            table.addCell(String.valueOf(item.ordemNaFila()));
            table.addCell(item.resultado().getDescricao());
        }

        document.add(table);
    }

    private void adicionarRodape(Document document) {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Relatório gerado pelo Sistema Vaquejada Digital")
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setItalic());
    }

    private Cell createHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
    }
}
