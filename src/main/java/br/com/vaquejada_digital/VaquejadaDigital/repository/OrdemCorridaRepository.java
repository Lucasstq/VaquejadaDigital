package br.com.vaquejada_digital.VaquejadaDigital.repository;

import br.com.vaquejada_digital.VaquejadaDigital.entity.OrdemCorrida;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.StatusChamada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdemCorridaRepository extends JpaRepository<OrdemCorrida, Long> {
    List<OrdemCorrida> findByRodizioIdOrderByPosicaoAsc(Long rodizioId);

    Optional<OrdemCorrida> findFirstByRodizioIdAndStatusChamadaOrderByPosicaoAsc(
            Long rodizioId,
            StatusChamada statusChamada
    );

    List<OrdemCorrida> findBySenhaId(Long senhaId);

    Long countByRodizioIdAndStatusChamada(Long rodizioId, StatusChamada statusChamada);
}
