package com.br.vaquejadadigital.repositories;

import com.br.vaquejadadigital.entities.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByUsuarioIdOrderByDataCriacaoDesc(Long usuarioId);

    List<Notificacao> findByUsuarioIdAndLidaOrderByDataCriacaoDesc(Long usuarioId, Boolean lida);

    @Query("SELECT COUNT(n) FROM Notificacao n WHERE n.usuario.id = :usuarioId AND n.lida = false")
    Long countNaoLidasByUsuarioId(@Param("usuarioId") Long usuarioId);
}

