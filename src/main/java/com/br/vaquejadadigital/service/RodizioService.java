package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.entities.Evento;
import com.br.vaquejadadigital.entities.Rodizio;
import com.br.vaquejadadigital.entities.enums.StatusRodizio;
import com.br.vaquejadadigital.repositories.EventoRepository;
import com.br.vaquejadadigital.repositories.RodizioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RodizioService {

    private final RodizioRepository rodizioRepository;
    private final EventoRepository eventoRepository;

    @Transactional
    public Rodizio criar(Rodizio rodizio) {
        Evento evento = eventoRepository.findById(rodizio.getEvento().getId())
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        if (rodizioRepository.findByEventoIdAndNumeroRodizio(evento.getId(), rodizio.getNumeroRodizio()).isPresent()) {
            throw new RuntimeException("Rodízio já existe para este número");
        }

        rodizio.setEvento(evento);
        rodizio.setStatus(StatusRodizio.AGUARDANDO);

        return rodizioRepository.save(rodizio);
    }

    @Transactional(readOnly = true)
    public List<Rodizio> listarPorEvento(Long eventoId) {
        return rodizioRepository.findByEventoIdOrderByNumeroRodizioAsc(eventoId);
    }

    @Transactional(readOnly = true)
    public Rodizio buscarPorId(Long id) {
        return rodizioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rodízio não encontrado"));
    }

    @Transactional
    public Rodizio iniciarRodizio(Long id) {
        Rodizio rodizio = buscarPorId(id);
        rodizio.setStatus(StatusRodizio.EM_ANDAMENTO);
        rodizio.setDataInicio(LocalDateTime.now());
        return rodizioRepository.save(rodizio);
    }

    @Transactional
    public Rodizio finalizarRodizio(Long id) {
        Rodizio rodizio = buscarPorId(id);
        rodizio.setStatus(StatusRodizio.FINALIZADO);
        rodizio.setDataFim(LocalDateTime.now());
        return rodizioRepository.save(rodizio);
    }
}
