package br.com.vaquejada_digital.VaquejadaDigital.controller;

import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.UsuariosResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.PromocaoRequest;
import br.com.vaquejada_digital.VaquejadaDigital.mapper.UsuariosMapper;
import br.com.vaquejada_digital.VaquejadaDigital.service.UsuariosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vaquejada-digital/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UsuariosService service;

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarPerfil(@PathVariable Long id, @Valid @RequestBody PromocaoRequest promocaoRequest) {
        service.promoverUsuario(id, promocaoRequest.novoPerfil());
        return ResponseEntity.ok("perfil atualizado com sucesso. ");
    }

    @GetMapping
    public ResponseEntity<List<UsuariosResponse>> listarUsuarios() {
        List<UsuariosResponse> list = service.findAll().stream()
                .map(UsuariosMapper::toUserResponse)
                .toList();
        return ResponseEntity.ok(list);
    }
}
