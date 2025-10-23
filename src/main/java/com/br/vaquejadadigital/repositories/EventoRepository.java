package com.br.vaquejadadigital.repositories;

import com.br.vaquejadadigital.entities.Evento;
import com.br.vaquejadadigital.entities.enums.StatusEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByStatus(StatusEvento status);

    @Query("SELECT e FROM Evento e WHERE e.dataInicio >= :dataInicio AND e.dataFim <= :dataFim")
    List<Evento> findEventosPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim);

    @Query("SELECT e FROM Evento e WHERE e.status = 'CRIADO' OR e.status = 'EM_ANDAMENTO'")
    List<Evento> findEventosAtivos();
}
