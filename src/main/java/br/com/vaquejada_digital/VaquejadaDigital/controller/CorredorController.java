package br.com.vaquejada_digital.VaquejadaDigital.controller;

import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.CorredorResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.CorredorRequest;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Corredor;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import br.com.vaquejada_digital.VaquejadaDigital.mapper.CorredorMapper;
import br.com.vaquejada_digital.VaquejadaDigital.service.CorredorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vaquejada-digital/corredor")
@RequiredArgsConstructor
public class CorredorController {

    private final CorredorService corredorService;

    @GetMapping("/meu-perfil")
    @PreAuthorize("hasRole('CORREDOR')")
    public ResponseEntity<CorredorResponse> buscarMeuPerfil(Authentication authentication) {
        Usuarios usuario = (Usuarios) authentication.getPrincipal();
        Corredor corredor = corredorService.buscarPorUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Perfil de corredor não encontrado"));

        return ResponseEntity.ok(CorredorMapper.toCorredorResponse(corredor));
    }

    @PostMapping("/completar-perfil")
    @PreAuthorize("hasRole('CORREDOR')")
    public ResponseEntity<CorredorResponse> completarCadastro(@Valid @RequestBody CorredorRequest corredorRequest, Authentication authentication) {
        System.out.println("Authentication: " + authentication);
        System.out.println("Principal: " + authentication.getPrincipal());
        System.out.println("Authorities: " + authentication.getAuthorities());
        Usuarios usuario = (Usuarios) authentication.getPrincipal();
        Corredor corredor = corredorService.atualizarPerfil(usuario.getId(), CorredorMapper.toCorredor(corredorRequest));
        return ResponseEntity.ok(CorredorMapper.toCorredorResponse(corredor));
    }
}
