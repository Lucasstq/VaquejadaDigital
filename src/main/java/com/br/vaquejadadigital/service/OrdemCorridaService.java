package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.entities.Dupla;
import com.br.vaquejadadigital.entities.OrdemCorrida;
import com.br.vaquejadadigital.entities.Rodizio;
import com.br.vaquejadadigital.entities.enums.StatusCorrida;
import com.br.vaquejadadigital.exception.BusinessException;
import com.br.vaquejadadigital.exception.ResourceNotFoundException;
import com.br.vaquejadadigital.repositories.DuplaRepository;
import com.br.vaquejadadigital.repositories.OrdemCorridaRepository;
import com.br.vaquejadadigital.repositories.RodizioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdemCorridaService {

    private final OrdemCorridaRepository ordemCorridaRepository;
    private final RodizioRepository rodizioRepository;
    private final DuplaRepository duplaRepository;
    private final NotificacaoService notificacaoService;

    @Transactional
    public OrdemCorrida adicionar(OrdemCorrida ordem) {
        log.info("Adicionando dupla {} na posição {} do rodízio {}",
                ordem.getDupla().getId(), ordem.getPosicao(), ordem.getRodizio().getId());

        Rodizio rodizio = rodizioRepository.findById(ordem.getRodizio().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Rodízio não encontrado"));

        Dupla dupla = duplaRepository.findById(ordem.getDupla().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Dupla não encontrada"));

        ordem.setRodizio(rodizio);
        ordem.setDupla(dupla);
        ordem.setStatus(StatusCorrida.PRONTO);

        OrdemCorrida savedOrdem = ordemCorridaRepository.save(ordem);
        log.debug("Ordem de corrida {} criada com sucesso", savedOrdem.getId());

        return savedOrdem;
    }

    @Transactional(readOnly = true)
    public List<OrdemCorrida> listarPorRodizio(Long rodizioId) {
        log.debug("Listando ordem de corrida do rodízio {}", rodizioId);
        return ordemCorridaRepository.findByRodizioIdOrderByPosicaoAsc(rodizioId);
    }

    /**
     * RF014 - Chamar dupla para pista (Locutor)
     */
    @Transactional
    public OrdemCorrida chamarDupla(Long id) {
        log.info("Chamando dupla - Ordem de corrida ID: {}", id);
        OrdemCorrida ordem = ordemCorridaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de corrida não encontrada"));

        if (ordem.getStatus() != StatusCorrida.PRONTO) {
            throw new BusinessException("Dupla não está pronta para ser chamada. Status atual: " + ordem.getStatus());
        }

        ordem.setStatus(StatusCorrida.CHAMADO);
        ordem.setDataChamada(LocalDateTime.now());

        OrdemCorrida savedOrdem = ordemCorridaRepository.save(ordem);

        // RF018 - Notificar corredores da chamada
        String nomeDupla = montarNomeDupla(ordem.getDupla());
        Long puxadorUsuarioId = ordem.getDupla().getPuxador().getUsuario().getId();
        Long esteireiroUsuarioId = ordem.getDupla().getEsteireiro().getUsuario().getId();

        notificacaoService.notificarChamada(puxadorUsuarioId, nomeDupla, ordem.getId());
        notificacaoService.notificarChamada(esteireiroUsuarioId, nomeDupla, ordem.getId());

        log.info("Dupla {} chamada com sucesso", nomeDupla);
        return savedOrdem;
    }

    @Transactional
    public OrdemCorrida marcarComoCorrendo(Long id) {
        log.info("Marcando dupla como correndo - Ordem ID: {}", id);
        OrdemCorrida ordem = ordemCorridaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de corrida não encontrada"));

        ordem.setStatus(StatusCorrida.CORRENDO);
        return ordemCorridaRepository.save(ordem);
    }

    @Transactional
    public OrdemCorrida marcarComoCorreu(Long id) {
        log.info("Marcando dupla como correu - Ordem ID: {}", id);
        OrdemCorrida ordem = ordemCorridaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de corrida não encontrada"));

        ordem.setStatus(StatusCorrida.CORREU);
        ordem.setDataCorrida(LocalDateTime.now());
        return ordemCorridaRepository.save(ordem);
    }

    /**
     * RF014 - Marcar dupla como falta
     * RF005 - A dupla será adicionada ao Rabo da Gata
     */
    @Transactional
    public OrdemCorrida marcarComoFalta(Long id) {
        log.info("Marcando dupla como falta - Ordem ID: {}", id);
        OrdemCorrida ordem = ordemCorridaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de corrida não encontrada"));

        ordem.setStatus(StatusCorrida.FALTA);
        OrdemCorrida savedOrdem = ordemCorridaRepository.save(ordem);

        // RF020 - Notificar corredores sobre inclusão no Rabo da Gata
        Long eventoId = ordem.getRodizio().getEvento().getId();
        Long puxadorUsuarioId = ordem.getDupla().getPuxador().getUsuario().getId();
        Long esteireiroUsuarioId = ordem.getDupla().getEsteireiro().getUsuario().getId();

        notificacaoService.notificarRaboDaGata(puxadorUsuarioId, eventoId);
        notificacaoService.notificarRaboDaGata(esteireiroUsuarioId, eventoId);

        log.info("Dupla marcada como falta e notificada sobre Rabo da Gata");
        return savedOrdem;
    }

    /**
     * RF005 - Listar duplas que faltaram (para Rabo da Gata)
     */
    @Transactional(readOnly = true)
    public List<OrdemCorrida> listarFaltas(Long rodizioId) {
        log.debug("Listando duplas com falta do rodízio {}", rodizioId);
        return ordemCorridaRepository.findByRodizioAndStatus(rodizioId, StatusCorrida.FALTA);
    }

    private String montarNomeDupla(Dupla dupla) {
        if (dupla.getNomeDupla() != null && !dupla.getNomeDupla().isBlank()) {
            return dupla.getNomeDupla();
        }
        return dupla.getPuxador().getUsuario().getNome() + " / " +
                dupla.getEsteireiro().getUsuario().getNome();
    }
}

