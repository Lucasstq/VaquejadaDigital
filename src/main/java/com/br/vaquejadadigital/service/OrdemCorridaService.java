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

    //Permite que o locutor mova uma dupla para outra posição na fila, Reordena automaticamente as outras duplas afetadas
    @Transactional
    public OrdemCorrida alterarPosicao(Long ordemId, Integer novaPosicao) {
        log.info("Alterando posição da ordem {} para posição {}", ordemId, novaPosicao);

        OrdemCorrida ordemParaMover = ordemCorridaRepository.findById(ordemId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de corrida não encontrada"));

        Integer posicaoAtual = ordemParaMover.getPosicao();
        Long rodizioId = ordemParaMover.getRodizio().getId();

        if (ordemParaMover.getStatus() == StatusCorrida.CORREU) {
            throw new BusinessException("Não é possível alterar a posição de uma dupla que já correu");
        }

        if (posicaoAtual.equals(novaPosicao)) {
            log.debug("Posição não foi alterada, retornando ordem atual");
            return ordemParaMover;
        }

        List<OrdemCorrida> todasOrdens = ordemCorridaRepository.findByRodizioIdOrderByPosicaoAsc(rodizioId);

        if (novaPosicao < 1 || novaPosicao > todasOrdens.size()) {
            throw new BusinessException(
                    String.format("Posição inválida. Deve estar entre 1 e %d", todasOrdens.size())
            );
        }

        todasOrdens.remove(ordemParaMover);

        todasOrdens.add(novaPosicao - 1, ordemParaMover);

        for (int i = 0; i < todasOrdens.size(); i++) {
            OrdemCorrida ordem = todasOrdens.get(i);
            Integer novaPosicaoOrdem = i + 1;

            if (!ordem.getPosicao().equals(novaPosicaoOrdem)) {
                ordem.setPosicao(novaPosicaoOrdem);
                ordemCorridaRepository.save(ordem);
                Long puxadorUsuarioId = ordem.getDupla().getPuxador().getUsuario().getId();
                Long esteireiroUsuarioId = ordem.getDupla().getEsteireiro().getUsuario().getId();

                notificacaoService.notificarAlteracaoOrdem(puxadorUsuarioId, novaPosicaoOrdem, rodizioId);
                notificacaoService.notificarAlteracaoOrdem(esteireiroUsuarioId, novaPosicaoOrdem, rodizioId);

                log.debug("Ordem {} reposicionada para posição {}", ordem.getId(), novaPosicaoOrdem);
            }
        }

        log.info("Ordem {} movida de posição {} para posição {} com sucesso",
                ordemId, posicaoAtual, novaPosicao);

        return ordemCorridaRepository.findById(ordemId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem não encontrada"));
    }

    //Adiciona uma dupla em uma posição específica, deslocando as demais
    @Transactional
    public OrdemCorrida encaixarDupla(Long rodizioId, Long duplaId, Integer posicao) {
        log.info("Encaixando dupla {} na posição {} do rodízio {}", duplaId, posicao, rodizioId);

        Rodizio rodizio = rodizioRepository.findById(rodizioId)
                .orElseThrow(() -> new ResourceNotFoundException("Rodízio não encontrado"));

        Dupla dupla = duplaRepository.findById(duplaId)
                .orElseThrow(() -> new ResourceNotFoundException("Dupla não encontrada"));

        List<OrdemCorrida> todasOrdens = ordemCorridaRepository.findByRodizioIdOrderByPosicaoAsc(rodizioId);

        if (posicao < 1 || posicao > todasOrdens.size() + 1) {
            throw new BusinessException(
                    String.format("Posição inválida. Deve estar entre 1 e %d", todasOrdens.size() + 1)
            );
        }

        OrdemCorrida novaOrdem = OrdemCorrida.builder()
                .rodizio(rodizio)
                .dupla(dupla)
                .posicao(posicao)
                .status(StatusCorrida.PRONTO)
                .build();

        for (OrdemCorrida ordem : todasOrdens) {
            if (ordem.getPosicao() >= posicao) {
                ordem.setPosicao(ordem.getPosicao() + 1);
                ordemCorridaRepository.save(ordem);
                Long puxadorUsuarioId = ordem.getDupla().getPuxador().getUsuario().getId();
                Long esteireiroUsuarioId = ordem.getDupla().getEsteireiro().getUsuario().getId();
                notificacaoService.notificarAlteracaoOrdem(puxadorUsuarioId, ordem.getPosicao(), rodizioId);
                notificacaoService.notificarAlteracaoOrdem(esteireiroUsuarioId, ordem.getPosicao(), rodizioId);
            }
        }
        OrdemCorrida savedOrdem = ordemCorridaRepository.save(novaOrdem);
        log.info("Dupla {} encaixada com sucesso na posição {}", duplaId, posicao);
        return savedOrdem;
    }

    //Reintegra duplas que faltaram ao final do rodízio
    @Transactional
    public List<OrdemCorrida> processarRaboDaGata(Long rodizioId) {
        log.info("Processando Rabo da Gata para rodízio {}", rodizioId);
        List<OrdemCorrida> faltas = listarFaltas(rodizioId);

        if (faltas.isEmpty()) {
            log.info("Nenhuma falta encontrada para o rodízio {}", rodizioId);
            return List.of();
        }
        List<OrdemCorrida> todasOrdens = ordemCorridaRepository.findByRodizioIdOrderByPosicaoAsc(rodizioId);

        Integer maiorPosicao = todasOrdens.stream()
                .map(OrdemCorrida::getPosicao)
                .max(Integer::compareTo)
                .orElse(0);

        int novaPosicao = maiorPosicao + 1;
        for (OrdemCorrida falta : faltas) {
            falta.setPosicao(novaPosicao);
            falta.setStatus(StatusCorrida.PRONTO);
            ordemCorridaRepository.save(falta);

            Long puxadorUsuarioId = falta.getDupla().getPuxador().getUsuario().getId();
            Long esteireiroUsuarioId = falta.getDupla().getEsteireiro().getUsuario().getId();

            notificacaoService.notificarAlteracaoOrdem(puxadorUsuarioId, novaPosicao, rodizioId);
            notificacaoService.notificarAlteracaoOrdem(esteireiroUsuarioId, novaPosicao, rodizioId);

            log.info("Dupla {} reposicionada para posição {} (Rabo da Gata)",
                    falta.getDupla().getId(), novaPosicao);

            novaPosicao++;
        }

        log.info("Rabo da Gata processado: {} duplas reintegradas", faltas.size());
        return faltas;
    }

    @Transactional(readOnly = true)
    public OrdemCorrida buscarPorId(Long id) {
        return ordemCorridaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de corrida não encontrada"));
    }
}

