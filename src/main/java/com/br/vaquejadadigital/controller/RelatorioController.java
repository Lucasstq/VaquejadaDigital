package com.br.vaquejadadigital.controller;

import com.br.vaquejadadigital.dtos.request.RelatorioFiltroRequest;
import com.br.vaquejadadigital.dtos.response.RelatorioResultadosResponse;
import com.br.vaquejadadigital.service.CsvExportService;
import com.br.vaquejadadigital.service.PdfExportService;
import com.br.vaquejadadigital.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('ADMIN')")
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final PdfExportService pdfExportService;
    private final CsvExportService csvExportService;

    private static final DateTimeFormatter FILE_NAME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    //Gera relatório de resultados (JSON)
    @GetMapping("/resultados")
    public ResponseEntity<RelatorioResultadosResponse> gerarRelatorio(
            @RequestParam Long eventoId,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Long corredorId,
            @RequestParam(required = false) Long rodizioId,
            @RequestParam(required = false) String tipoResultado) {

        log.info("Gerando relatório para evento {}", eventoId);

        RelatorioFiltroRequest filtro = new RelatorioFiltroRequest(
                eventoId,
                categoria != null ? com.br.vaquejadadigital.entities.enums.TipoCategoria.valueOf(categoria) : null,
                corredorId,
                rodizioId,
                tipoResultado != null ? com.br.vaquejadadigital.entities.enums.TipoResultado.valueOf(tipoResultado) : null,
                "JSON"
        );

        RelatorioResultadosResponse relatorio = relatorioService.gerarRelatorio(filtro);

        return ResponseEntity.ok(relatorio);
    }

    //Exporta relatório em PDF
    @GetMapping("/resultados/pdf")
    public ResponseEntity<byte[]> exportarPdf(
            @RequestParam Long eventoId,
            @RequestParam(required = false) String fase,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Long corredorId,
            @RequestParam(required = false) Long rodizioId,
            @RequestParam(required = false) String tipoResultado) {

        log.info("Exportando relatório PDF para evento {}", eventoId);

        // Gerar relatório
        RelatorioFiltroRequest filtro = new RelatorioFiltroRequest(
                eventoId,
                categoria != null ? com.br.vaquejadadigital.entities.enums.TipoCategoria.valueOf(categoria) : null,
                corredorId,
                rodizioId,
                tipoResultado != null ? com.br.vaquejadadigital.entities.enums.TipoResultado.valueOf(tipoResultado) : null,
                "PDF"
        );

        RelatorioResultadosResponse relatorio = relatorioService.gerarRelatorio(filtro);

        // Exportar para PDF
        byte[] pdfBytes = pdfExportService.exportarParaPdf(relatorio);

        // Nome do arquivo
        String fileName = String.format("relatorio_resultados_%s_%s.pdf",
                eventoId,
                LocalDateTime.now().format(FILE_NAME_FORMATTER));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    //Exporta relatório em CSV
    @GetMapping("/resultados/csv")
    public ResponseEntity<byte[]> exportarCsv(
            @RequestParam Long eventoId,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Long corredorId,
            @RequestParam(required = false) Long rodizioId,
            @RequestParam(required = false) String tipoResultado) {

        log.info("Exportando relatório CSV para evento {}", eventoId);

        // Gerar relatório
        RelatorioFiltroRequest filtro = new RelatorioFiltroRequest(
                eventoId,
                categoria != null ? com.br.vaquejadadigital.entities.enums.TipoCategoria.valueOf(categoria) : null,
                corredorId,
                rodizioId,
                tipoResultado != null ? com.br.vaquejadadigital.entities.enums.TipoResultado.valueOf(tipoResultado) : null,
                "CSV"
        );

        RelatorioResultadosResponse relatorio = relatorioService.gerarRelatorio(filtro);

        // Exportar para CSV
        byte[] csvBytes = csvExportService.exportarParaCsv(relatorio);

        // Nome do arquivo
        String fileName = String.format("relatorio_resultados_%s_%s.csv",
                eventoId,
                LocalDateTime.now().format(FILE_NAME_FORMATTER));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "csv", java.nio.charset.StandardCharsets.UTF_8));
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentLength(csvBytes.length);

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

     //Relatório por corredor específico
    @GetMapping("/corredor/{corredorId}")
    public ResponseEntity<RelatorioResultadosResponse> relatorioPorCorredor(
            @PathVariable Long corredorId,
            @RequestParam Long eventoId) {

        log.info("Gerando relatório do corredor {} no evento {}", corredorId, eventoId);

        RelatorioFiltroRequest filtro = new RelatorioFiltroRequest(
                eventoId,
                null,
                corredorId,
                null,
                null,
                "JSON"
        );

        RelatorioResultadosResponse relatorio = relatorioService.gerarRelatorio(filtro);

        return ResponseEntity.ok(relatorio);
    }

    //Relatório por rodízio específico
    @GetMapping("/rodizio/{rodizioId}")
    public ResponseEntity<RelatorioResultadosResponse> relatorioPorRodizio(
            @PathVariable Long rodizioId,
            @RequestParam Long eventoId) {

        log.info("Gerando relatório do rodízio {} no evento {}", rodizioId, eventoId);

        RelatorioFiltroRequest filtro = new RelatorioFiltroRequest(
                eventoId,
                null,
                null,
                rodizioId,
                null,
                "JSON"
        );

        RelatorioResultadosResponse relatorio = relatorioService.gerarRelatorio(filtro);

        return ResponseEntity.ok(relatorio);
    }
}
