package com.br.vaquejadadigital.repositories;

import com.br.vaquejadadigital.entities.Corredor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CorredorRepository extends JpaRepository<Corredor, Long> {
    Optional<Corredor> findByUsuarioId(Long usuarioId);
    boolean existsByCpf(String cpf);
}
