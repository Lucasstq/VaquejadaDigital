package com.br.vaquejadadigital.repositories;

import com.br.vaquejadadigital.entities.HistoricoCorredor;
import com.br.vaquejadadigital.entities.enums.TipoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoricoCorredorRepository extends JpaRepository<HistoricoCorredor, Long> {

    //Busca histórico de um corredor em um evento específico
    Optional<HistoricoCorredor> findByCorredorIdAndEventoId(Long corredorId, Long eventoId);

    //Lista todo o histórico de um corredor ordenado por data
    List<HistoricoCorredor> findByCorredorIdOrderByDataCriacaoDesc(Long corredorId);

    //Lista histórico de um corredor em uma categoria específica
    List<HistoricoCorredor> findByCorredorIdAndCategoriaOrderByDataCriacaoDesc(Long corredorId, TipoCategoria categoria);

    //Lista todos os históricos de um evento
    List<HistoricoCorredor> findByEventoIdOrderByPosicaoFinalAsc(Long eventoId);

    //Verifica se já existe histórico para corredor e evento
    boolean existsByCorredorIdAndEventoId(Long corredorId, Long eventoId);

    //Calcula estatísticas agregadas de um corredor
    @Query("SELECT " +
            "SUM(h.totalCorridas) as totalCorridas, " +
            "SUM(h.valeBoi) as totalValeBoi, " +
            "SUM(h.zeros) as totalZeros, " +
            "SUM(h.retornos) as totalRetornos, " +
            "SUM(h.faltas) as totalFaltas, " +
            "SUM(h.desclassificacoes) as totalDesclassificacoes, " +
            "COUNT(h) as totalEventos " +
            "FROM HistoricoCorredor h " +
            "WHERE h.corredor.id = :corredorId")
    Object[] calcularEstatisticasGerais(@Param("corredorId") Long corredorId);

    //Busca os melhores resultados de um corredor (top N colocações)
    @Query("SELECT h FROM HistoricoCorredor h " +
            "WHERE h.corredor.id = :corredorId " +
            "AND h.posicaoFinal IS NOT NULL " +
            "ORDER BY h.posicaoFinal ASC")
    List<HistoricoCorredor> findMelhoresColocacoes(@Param("corredorId") Long corredorId);

    //Conta quantos eventos o corredor participou
    @Query("SELECT COUNT(h) FROM HistoricoCorredor h WHERE h.corredor.id = :corredorId")
    Long countEventosParticipados(@Param("corredorId") Long corredorId);

    //Busca históricos com taxa de sucesso acima de um valor
    @Query("SELECT h FROM HistoricoCorredor h " +
            "WHERE h.corredor.id = :corredorId " +
            "AND h.taxaSucesso >= :taxaMinima " +
            "ORDER BY h.taxaSucesso DESC")
    List<HistoricoCorredor> findByTaxaSucessoMaiorQue(@Param("corredorId") Long corredorId,
                                                      @Param("taxaMinima") Double taxaMinima);
}
