package br.com.vaquejada_digital.VaquejadaDigital.repository;

import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {

    Optional<Usuarios> findByEmail(String email);

}
