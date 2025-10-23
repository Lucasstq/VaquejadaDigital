package com.br.vaquejadadigital.repositories;

import com.br.vaquejadadigital.entities.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    List<ItemVenda> findByVendaId(Long vendaId);
}
