package br.com.vaquejada_digital.VaquejadaDigital.service;

import br.com.vaquejada_digital.VaquejadaDigital.entity.Evento;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.Status;
import br.com.vaquejada_digital.VaquejadaDigital.exceptions.EventoNotFoundException;
import br.com.vaquejada_digital.VaquejadaDigital.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UsuariosService usuariosService;

    public Evento createEvent(Evento event, List<Long> juizId, List<Long> locutorId) {

        List<Usuarios> juizes = usuariosService.buscarJuizesById(juizId);
        List<Usuarios> locutores = usuariosService.buscarLocutoresById(locutorId);

        event.setJuizes(juizes);
        event.setLocutores(locutores);
        event.setStatus(Status.ATIVO);
        return eventoRepository.save(event);
    }

    public Evento updateEvent(Long id, Evento updateEvent) {
        Evento updatedEvent = eventoRepository.findById(id)
                .orElseThrow(() -> new EventoNotFoundException("Evento não encontrado."));

        updatedEvent.setNome(updateEvent.getNome());
        updatedEvent.setDescricao(updateEvent.getDescricao());
        updatedEvent.setLocal(updateEvent.getLocal());
        updatedEvent.setPrecoBaseSenha(updateEvent.getPrecoBaseSenha());
        updatedEvent.setQuantidadeTotalSenhas(updateEvent.getQuantidadeTotalSenhas());
        updatedEvent.setImagensVideos(updateEvent.getImagensVideos());
        updatedEvent.setStatus(updateEvent.getStatus());
        eventoRepository.save(updatedEvent);
        return updatedEvent;


    }

    public List<Evento> findAll() {
        return eventoRepository.findAll();
    }

    public List<Evento> findByStatus() {
        return eventoRepository.findByStatus(Status.ATIVO);
    }

    public Evento findById(Long id) {
        return eventoRepository.findById(id).orElseThrow(() -> new EventoNotFoundException("Evento não encontrado."));
    }

    public Evento updateStatus(Long id, Status status) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EventoNotFoundException("Evento não encontrado."));
        evento.setStatus(status);
        return eventoRepository.save(evento);
    }
}
