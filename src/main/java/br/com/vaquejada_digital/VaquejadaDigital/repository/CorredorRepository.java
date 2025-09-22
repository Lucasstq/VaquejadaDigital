package br.com.vaquejada_digital.VaquejadaDigital.repository;

import br.com.vaquejada_digital.VaquejadaDigital.entity.Corredor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CorredorRepository extends JpaRepository<Corredor, Long> {

    public Optional<Corredor> findByUsuarioId(Long usuarioId);
}
