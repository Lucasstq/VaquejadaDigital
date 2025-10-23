package com.br.vaquejadadigital.controller;

import com.br.vaquejadadigital.dtos.response.NotificacaoResponse;
import com.br.vaquejadadigital.entities.Notificacao;
import com.br.vaquejadadigital.entities.Usuario;
import com.br.vaquejadadigital.mapper.NotificacaoMapper;
import com.br.vaquejadadigital.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificacoes")
@RequiredArgsConstructor
@Slf4j
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    @GetMapping
    public ResponseEntity<List<NotificacaoResponse>> listarMinhasNotificacoes() {
        Long usuarioId = getUsuarioLogadoId();
        log.info("Listando notificações do usuário {}", usuarioId);

        List<NotificacaoResponse> response = notificacaoService.listarPorUsuario(usuarioId).stream()
                .map(NotificacaoMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/nao-lidas")
    public ResponseEntity<List<NotificacaoResponse>> listarNaoLidas() {
        Long usuarioId = getUsuarioLogadoId();
        log.info("Listando notificações não lidas do usuário {}", usuarioId);

        List<NotificacaoResponse> response = notificacaoService.listarNaoLidas(usuarioId).stream()
                .map(NotificacaoMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/nao-lidas/count")
    public ResponseEntity<Long> contarNaoLidas() {
        Long usuarioId = getUsuarioLogadoId();
        log.debug("Contando notificações não lidas do usuário {}", usuarioId);

        Long count = notificacaoService.contarNaoLidas(usuarioId);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{id}/marcar-lida")
    public ResponseEntity<NotificacaoResponse> marcarComoLida(@PathVariable Long id) {
        log.info("Marcando notificação {} como lida", id);

        Notificacao notificacao = notificacaoService.marcarComoLida(id);
        return ResponseEntity.ok(NotificacaoMapper.toResponse(notificacao));
    }

    @PutMapping("/marcar-todas-lidas")
    public ResponseEntity<Void> marcarTodasComoLidas() {
        Long usuarioId = getUsuarioLogadoId();
        log.info("Marcando todas as notificações do usuário {} como lidas", usuarioId);

        notificacaoService.marcarTodasComoLidas(usuarioId);
        return ResponseEntity.noContent().build();
    }

    private Long getUsuarioLogadoId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return usuario.getId();
    }
}

