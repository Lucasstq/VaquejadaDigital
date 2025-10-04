package br.com.vaquejada_digital.VaquejadaDigital.repository;

import br.com.vaquejada_digital.VaquejadaDigital.entity.Rodizio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RodizioRepository extends JpaRepository<Rodizio, Long> {



    List<Rodizio> findByEventoIdOrderByNumeroRodizioAsc(Long eventoId);

    @Query("SELECT MAX(r.numeroRodizio) FROM Rodizio r WHERE r.evento.id = :eventoId AND r.categoria.id = :categoriaId")
    Optional<Integer> findMaxNumeroRodizioByEventoAndCategoria(
            @Param("eventoId") Long eventoId,
            @Param("categoriaId") Long categoriaId
    );

    List<Rodizio> findByEventoIdAndCategoriaIdOrderByNumeroRodizioAsc(Long eventoId, Long categoriaId);

    Optional<Rodizio> findByEventoIdAndNumeroRodizio(Long eventoId, Integer numeroRodizio);
}
