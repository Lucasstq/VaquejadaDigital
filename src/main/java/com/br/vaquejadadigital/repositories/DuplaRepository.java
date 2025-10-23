package com.br.vaquejadadigital.repositories;

import com.br.vaquejadadigital.entities.Dupla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DuplaRepository extends JpaRepository<Dupla, Long> {

    /*
    procura dupla por puxador e esteiro, passando como parametro o id
    como se fosse um where do sql
     */
    @Query("SELECT d FROM Dupla d WHERE " +
            "(d.puxador.id = :puxadorId AND d.esteireiro.id = :esteireiroId) OR " +
            "(d.puxador.id = :esteireiroId AND d.esteireiro.id = :puxadorId)")
    Optional<Dupla> findByPuxadorAndEsteireiro(@Param("puxadorId") Long puxadorId,
                                               @Param("esteireiroId") Long esteireiroId);

    List<Dupla> findByPuxadorId(Long puxadorId);
    List<Dupla> findByEsteireiroId(Long esteireiroId);
}
