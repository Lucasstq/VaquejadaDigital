package com.br.vaquejadadigital.repositories;

import com.br.vaquejadadigital.entities.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Long> {
    Optional<Resultado> findByOrdemCorridaId(Long ordemCorridaId);

    @Query("SELECT r FROM Resultado r " +
            "JOIN r.ordemCorrida oc " +
            "JOIN oc.rodizio rod " +
            "WHERE rod.evento.id = :eventoId")
    List<Resultado> findByEventoId(@Param("eventoId") Long eventoId);

    @Query("SELECT r FROM Resultado r " +
            "JOIN r.ordemCorrida oc " +
            "WHERE oc.dupla.id = :duplaId")
    List<Resultado> findByDuplaId(@Param("duplaId") Long duplaId);
}