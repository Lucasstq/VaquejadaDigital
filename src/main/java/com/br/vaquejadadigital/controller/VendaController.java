package com.br.vaquejadadigital.controller;


import com.br.vaquejadadigital.dtos.request.VendaManualRequest;
import com.br.vaquejadadigital.dtos.request.VendaOnlineRequest;
import com.br.vaquejadadigital.dtos.response.VendaResponse;
import com.br.vaquejadadigital.entities.ItemVenda;
import com.br.vaquejadadigital.entities.Venda;
import com.br.vaquejadadigital.mapper.VendaMapper;
import com.br.vaquejadadigital.service.DuplaService;
import com.br.vaquejadadigital.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;
    private final VendaMapper vendaMapper;
    private final DuplaService duplaService;

    @PostMapping("/manual")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<VendaResponse> vendaManual(@Valid @RequestBody VendaManualRequest request) {
        Venda venda = vendaMapper.toVenda(request);
        // Prepara itens com duplas
        List<ItemVenda> itens = request.itens().stream()
                .map(itemReq -> {
                    // Buscar ou criar dupla
                    var dupla = duplaService.buscarOuCriarDupla(
                            itemReq.puxadorId(),
                            itemReq.esteireiroId()
                    );

                    return vendaMapper.toItemVenda(itemReq, dupla);
                })
                .collect(Collectors.toList());

        Venda vendaSalva = vendaService.vendaManual(venda, itens);

        return ResponseEntity.status(HttpStatus.CREATED).body(vendaMapper.toResponse(vendaSalva));
    }

    @PostMapping("/online")
    public ResponseEntity<VendaResponse> vendaOnline(@Valid @RequestBody VendaOnlineRequest request) {
        Venda venda = vendaMapper.toVenda(request);

        List<ItemVenda> itens = request.itens().stream()
                .map(itemReq -> {
                    var dupla = duplaService.buscarOuCriarDupla(
                            itemReq.puxadorId(),
                            itemReq.esteireiroId()
                    );
                    return vendaMapper.toItemVenda(itemReq, dupla);
                })
                .collect(Collectors.toList());

        Venda vendaSalva = vendaService.vendaOnline(venda, itens);
        VendaResponse response = vendaMapper.toResponse(vendaSalva);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/evento/{eventoId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<VendaResponse>> listarPorEvento(@PathVariable Long eventoId) {
        List<Venda> vendas = vendaService.listarPorEvento(eventoId);

        List<VendaResponse> response = vendas.stream()
                .map(vendaMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/comprador/{compradorId}")
    public ResponseEntity<List<VendaResponse>> listarPorComprador(@PathVariable Long compradorId) {
        List<Venda> vendas = vendaService.listarPorComprador(compradorId);

        List<VendaResponse> response = vendas.stream()
                .map(vendaMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponse> buscarPorId(@PathVariable Long id) {
        Venda venda = vendaService.buscarPorId(id);
        VendaResponse response = vendaMapper.toResponse(venda);
        return ResponseEntity.ok(response);
    }
}
