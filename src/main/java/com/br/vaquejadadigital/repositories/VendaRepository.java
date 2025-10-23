package com.br.vaquejadadigital.repositories;

import com.br.vaquejadadigital.entities.Venda;
import com.br.vaquejadadigital.entities.enums.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByEventoId(Long eventoId);

    List<Venda> findByCompradorId(Long compradorId);

    List<Venda> findByStatusPagamento(StatusPagamento status);

    @Query("SELECT v FROM Venda v WHERE v.evento.id = :eventoId " +
            "AND v.statusPagamento = :status")
    List<Venda> findByEventoAndStatus(@Param("eventoId") Long eventoId,
                                      @Param("status") StatusPagamento status);

    @Query("SELECT SUM(v.valorTotal) FROM Venda v WHERE v.evento.id = :eventoId " +
            "AND v.statusPagamento = 'APROVADO'")
    BigDecimal calcularReceitaEvento(@Param("eventoId") Long eventoId);
}

