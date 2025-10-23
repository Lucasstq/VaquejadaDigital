package com.br.vaquejadadigital.repositories;

import com.br.vaquejadadigital.entities.Senha;
import com.br.vaquejadadigital.entities.enums.StatusSenha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SenhaRepository extends JpaRepository<Senha, Long> {
    List<Senha> findByEventoId(Long eventoId);

    List<Senha> findByEventoIdAndCategoriaId(Long eventoId, Long categoriaId);

    List<Senha> findByEventoIdAndStatus(Long eventoId, StatusSenha status);

    @Query("SELECT s FROM Senha s WHERE s.evento.id = :eventoId " +
            "AND s.categoria.id = :categoriaId AND s.status = :status")
    List<Senha> findByEventoAndCategoriaAndStatus(@Param("eventoId") Long eventoId,
                                                  @Param("categoriaId") Long categoriaId,
                                                  @Param("status") StatusSenha status);

    @Query("SELECT COUNT(s) FROM Senha s WHERE s.evento.id = :eventoId AND s.status = 'VENDIDA'")
    Long countSenhasVendidasByEvento(@Param("eventoId") Long eventoId);
}
