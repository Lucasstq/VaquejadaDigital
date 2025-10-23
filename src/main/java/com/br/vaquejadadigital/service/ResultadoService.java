package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.entities.OrdemCorrida;
import com.br.vaquejadadigital.entities.Resultado;
import com.br.vaquejadadigital.entities.Usuario;
import com.br.vaquejadadigital.entities.enums.StatusCorrida;
import com.br.vaquejadadigital.entities.enums.TipoResultado;
import com.br.vaquejadadigital.exception.BusinessException;
import com.br.vaquejadadigital.exception.ResourceNotFoundException;
import com.br.vaquejadadigital.repositories.OrdemCorridaRepository;
import com.br.vaquejadadigital.repositories.ResultadoRepository;
import com.br.vaquejadadigital.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResultadoService {

    private final ResultadoRepository resultadoRepository;
    private final OrdemCorridaRepository ordemCorridaRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacaoService notificacaoService;

    /**
     * RF015 - Registrar resultado da corrida
     * RF006 - Gerenciar retornos automaticamente
     */
    @Transactional
    public Resultado registrar(Resultado resultado) {
        log.info("Registrando resultado para ordem de corrida {}", resultado.getOrdemCorrida().getId());

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario juiz = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        OrdemCorrida ordem = ordemCorridaRepository.findById(resultado.getOrdemCorrida().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de corrida não encontrada"));

        // Validar se já existe resultado
        if (resultadoRepository.findByOrdemCorridaId(ordem.getId()).isPresent()) {
            throw new BusinessException("Resultado já registrado para esta corrida");
        }

        resultado.setOrdemCorrida(ordem);
        resultado.setJuiz(juiz);

        Resultado savedResultado = resultadoRepository.save(resultado);
        log.debug("Resultado {} salvo com sucesso", savedResultado.getId());

        // Atualizar status da ordem de corrida
        ordem.setStatus(StatusCorrida.CORREU);
        ordemCorridaRepository.save(ordem);

        // RF006 - Se resultado for RETORNO, notificar corredor
        if (resultado.getTipoResultado() == TipoResultado.RETORNO) {
            Long eventoId = ordem.getRodizio().getEvento().getId();
            Long puxadorUsuarioId = ordem.getDupla().getPuxador().getUsuario().getId();
            Long esteireiroUsuarioId = ordem.getDupla().getEsteireiro().getUsuario().getId();

            notificacaoService.notificarRetorno(puxadorUsuarioId, eventoId);
            notificacaoService.notificarRetorno(esteireiroUsuarioId, eventoId);

            log.info("Corredores notificados sobre direito a retorno");
        }

        // RF020 - Notificar resultado registrado
        String resultadoDescricao = resultado.getTipoResultado().getDescricao();
        Long puxadorUsuarioId = ordem.getDupla().getPuxador().getUsuario().getId();
        Long esteireiroUsuarioId = ordem.getDupla().getEsteireiro().getUsuario().getId();

        notificacaoService.notificarResultado(puxadorUsuarioId, resultadoDescricao, ordem.getId());
        notificacaoService.notificarResultado(esteireiroUsuarioId, resultadoDescricao, ordem.getId());

        return savedResultado;
    }

    /**
     * RF006 - Listar resultados com retorno de um evento
     */
    @Transactional(readOnly = true)
    public List<Resultado> listarRetornosPorEvento(Long eventoId) {
        log.debug("Listando retornos do evento {}", eventoId);
        return resultadoRepository.findByEventoId(eventoId).stream()
                .filter(r -> r.getTipoResultado() == TipoResultado.RETORNO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Resultado> listarPorEvento(Long eventoId) {
        log.debug("Listando resultados do evento {}", eventoId);
        return resultadoRepository.findByEventoId(eventoId);
    }

    @Transactional(readOnly = true)
    public List<Resultado> listarPorDupla(Long duplaId) {
        log.debug("Listando resultados da dupla {}", duplaId);
        return resultadoRepository.findByDuplaId(duplaId);
    }

    @Transactional(readOnly = true)
    public Resultado buscarPorOrdemCorrida(Long ordemCorridaId) {
        log.debug("Buscando resultado da ordem de corrida {}", ordemCorridaId);
        return resultadoRepository.findByOrdemCorridaId(ordemCorridaId)
                .orElseThrow(() -> new ResourceNotFoundException("Resultado não encontrado para esta ordem de corrida"));
    }
}