package br.com.vaquejada_digital.VaquejadaDigital.controller;

import br.com.vaquejada_digital.VaquejadaDigital.entity.Senha;
import br.com.vaquejada_digital.VaquejadaDigital.service.SenhaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vaquejada-digital/senhas")
@RequiredArgsConstructor
public class SenhaController {

    private final SenhaService senhaService;

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<Senha>> listarSenhas(@PathVariable Long eventoId) {
        List<Senha> senhas = senhaService.listarSenhasDoEvento(eventoId);
        return ResponseEntity.ok(senhas);
    }
}