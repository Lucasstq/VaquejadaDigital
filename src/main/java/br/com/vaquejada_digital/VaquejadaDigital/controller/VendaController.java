package br.com.vaquejada_digital.VaquejadaDigital.controller;

import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.SenhaResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.ComprarSenhaRequest;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Corredor;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Senha;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import br.com.vaquejada_digital.VaquejadaDigital.mapper.SenhasMapper;
import br.com.vaquejada_digital.VaquejadaDigital.service.CorredorService;
import br.com.vaquejada_digital.VaquejadaDigital.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vaquejada-digital/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;
    private final CorredorService corredorService;

    @PostMapping("/comprar")
    @PreAuthorize("hasRole('CORREDOR')")
    public ResponseEntity<SenhaResponse> comprarSenha(
            @Valid @RequestBody ComprarSenhaRequest request) {

        Senha senha = vendaService.comprarSenha(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SenhasMapper.from(senha));
    }


    @GetMapping("/minhas")
    @PreAuthorize("hasRole('CORREDOR')")
    public ResponseEntity<List<SenhaResponse>> buscarMinhasSenhas(
            Authentication authentication) {

        Usuarios usuario = (Usuarios) authentication.getPrincipal();
        Corredor corredor = corredorService.buscarPorUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Corredor não encontrado"));

        List<SenhaResponse> senhas = vendaService.buscarSenhasDoCorretor(corredor.getId())
                .stream()
                .map(SenhasMapper::from)
                .toList();

        return ResponseEntity.ok(senhas);
    }
}