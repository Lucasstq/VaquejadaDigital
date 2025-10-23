package com.br.vaquejadadigital.repositories;

import com.br.vaquejadadigital.entities.Rodizio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RodizioRepository extends JpaRepository<Rodizio, Long> {
    List<Rodizio> findByEventoId(Long eventoId);

    List<Rodizio> findByEventoIdOrderByNumeroRodizioAsc(Long eventoId);

    Optional<Rodizio> findByEventoIdAndNumeroRodizio(Long eventoId, Integer numeroRodizio);

    @Query("SELECT r FROM Rodizio r WHERE r.evento.id = :eventoId " +
            "AND r.status = 'EM_ANDAMENTO'")
    Optional<Rodizio> findRodizioEmAndamento(Long eventoId);
}