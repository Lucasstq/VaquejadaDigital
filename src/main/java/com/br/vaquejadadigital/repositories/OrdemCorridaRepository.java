package com.br.vaquejadadigital.repositories;

import com.br.vaquejadadigital.entities.OrdemCorrida;
import com.br.vaquejadadigital.entities.enums.StatusCorrida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdemCorridaRepository extends JpaRepository<OrdemCorrida, Long> {
    List<OrdemCorrida> findByRodizioIdOrderByPosicaoAsc(Long rodizioId);

    List<OrdemCorrida> findByDuplaId(Long duplaId);

    @Query("SELECT oc FROM OrdemCorrida oc WHERE oc.rodizio.id = :rodizioId " +
            "AND oc.status = :status ORDER BY oc.posicao")
    List<OrdemCorrida> findByRodizioAndStatus(Long rodizioId, StatusCorrida status);
}
