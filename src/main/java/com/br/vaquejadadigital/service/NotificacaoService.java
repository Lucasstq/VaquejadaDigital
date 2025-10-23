package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.entities.Notificacao;
import com.br.vaquejadadigital.entities.Usuario;
import com.br.vaquejadadigital.entities.enums.TipoNotificacao;
import com.br.vaquejadadigital.exception.ResourceNotFoundException;
import com.br.vaquejadadigital.repositories.NotificacaoRepository;
import com.br.vaquejadadigital.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Criar e enviar notificação para um usuário
     */
    @Transactional
    public Notificacao enviarNotificacao(Long usuarioId, TipoNotificacao tipo, String mensagem,
                                         Long eventoId, Long rodizioId, Long ordemCorridaId) {
        log.info("Enviando notificação tipo {} para usuário {}", tipo, usuarioId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + usuarioId));

        Notificacao notificacao = Notificacao.builder()
                .usuario(usuario)
                .tipo(tipo)
                .mensagem(mensagem)
                .eventoId(eventoId)
                .rodizioId(rodizioId)
                .ordemCorridaId(ordemCorridaId)
                .lida(false)
                .build();

        notificacao = notificacaoRepository.save(notificacao);

        // TODO: Integrar com serviço de push notifications (Firebase, OneSignal, etc)
        // pushNotificationService.send(usuario.getDeviceToken(), mensagem);

        log.debug("Notificação {} criada com sucesso", notificacao.getId());
        return notificacao;
    }

    /**
     * Notificar chamada para pista
     */
    @Transactional
    public void notificarChamada(Long usuarioId, String nomeDupla, Long ordemCorridaId) {
        String mensagem = String.format("Atenção! A dupla %s foi chamada para a pista.", nomeDupla);
        enviarNotificacao(usuarioId, TipoNotificacao.CHAMADA_PISTA, mensagem, null, null, ordemCorridaId);
    }

    /**
     * Notificar alteração na ordem
     */
    @Transactional
    public void notificarAlteracaoOrdem(Long usuarioId, Integer novaPosicao, Long rodizioId) {
        String mensagem = String.format("Sua posição na ordem foi alterada. Nova posição: %d", novaPosicao);
        enviarNotificacao(usuarioId, TipoNotificacao.ALTERACAO_ORDEM, mensagem, null, rodizioId, null);
    }

    /**
     * Notificar inclusão em retorno
     */
    @Transactional
    public void notificarRetorno(Long usuarioId, Long eventoId) {
        String mensagem = "Você tem direito a retorno. Aguarde a chamada.";
        enviarNotificacao(usuarioId, TipoNotificacao.RETORNO, mensagem, eventoId, null, null);
    }

    /**
     * Notificar inclusão no Rabo da Gata
     */
    @Transactional
    public void notificarRaboDaGata(Long usuarioId, Long eventoId) {
        String mensagem = "Você foi incluído no Rabo da Gata. Prepare-se para a chamada.";
        enviarNotificacao(usuarioId, TipoNotificacao.RABO_DA_GATA, mensagem, eventoId, null, null);
    }

    /**
     * Notificar resultado registrado
     */
    @Transactional
    public void notificarResultado(Long usuarioId, String resultado, Long ordemCorridaId) {
        String mensagem = String.format("Resultado registrado: %s", resultado);
        enviarNotificacao(usuarioId, TipoNotificacao.RESULTADO_REGISTRADO, mensagem, null, null, ordemCorridaId);
    }

    /**
     * Listar notificações de um usuário
     */
    @Transactional(readOnly = true)
    public List<Notificacao> listarPorUsuario(Long usuarioId) {
        log.debug("Listando notificações do usuário {}", usuarioId);
        return notificacaoRepository.findByUsuarioIdOrderByDataCriacaoDesc(usuarioId);
    }

    /**
     * Listar notificações não lidas
     */
    @Transactional(readOnly = true)
    public List<Notificacao> listarNaoLidas(Long usuarioId) {
        log.debug("Listando notificações não lidas do usuário {}", usuarioId);
        return notificacaoRepository.findByUsuarioIdAndLidaOrderByDataCriacaoDesc(usuarioId, false);
    }

    /**
     * Contar notificações não lidas
     */
    @Transactional(readOnly = true)
    public Long contarNaoLidas(Long usuarioId) {
        return notificacaoRepository.countNaoLidasByUsuarioId(usuarioId);
    }

    /**
     * Marcar notificação como lida
     */
    @Transactional
    public Notificacao marcarComoLida(Long notificacaoId) {
        log.debug("Marcando notificação {} como lida", notificacaoId);
        Notificacao notificacao = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Notificação não encontrada com ID: " + notificacaoId));

        notificacao.setLida(true);
        notificacao.setDataLeitura(LocalDateTime.now());

        return notificacaoRepository.save(notificacao);
    }

    /**
     * Marcar todas as notificações de um usuário como lidas
     */
    @Transactional
    public void marcarTodasComoLidas(Long usuarioId) {
        log.info("Marcando todas as notificações do usuário {} como lidas", usuarioId);
        List<Notificacao> notificacoes = listarNaoLidas(usuarioId);
        LocalDateTime agora = LocalDateTime.now();

        notificacoes.forEach(n -> {
            n.setLida(true);
            n.setDataLeitura(agora);
        });

        notificacaoRepository.saveAll(notificacoes);
    }
}
