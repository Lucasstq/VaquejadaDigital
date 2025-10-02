package br.com.vaquejada_digital.VaquejadaDigital.repository;

import br.com.vaquejada_digital.VaquejadaDigital.entity.Senha;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SenhaRepository extends JpaRepository<Senha, Long> {

    List<Senha> findByEventoId(Long eventoId);

    List<Senha> findByEventoIdAndCategoriaId(Long eventoId, Long categoriaId);

    List<Senha> findByDuplaIdIn(List<Long> duplaIds);

    Optional<Senha> findByEventoIdAndCategoriaIdAndNumeroSenha(
            Long eventoId,
            Long categoriaId,
            Integer numeroSenha
    );

    @Query("""
        SELECT s FROM Senha s 
        WHERE s.evento.id = :eventoId 
          AND s.categoria.id = :categoriaId
          AND s.dupla IS NULL 
          AND s.bloqueada = false
        ORDER BY s.numeroSenha
        """)
    List<Senha> findDisponiveisByEventoAndCategoria(Long eventoId, Long categoriaId);

    Long countByEventoIdAndCategoriaIdAndStatusPagamento(
            Long eventoId,
            Long categoriaId,
            StatusPagamento status
    );

}
