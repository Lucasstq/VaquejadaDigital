package br.com.vaquejada_digital.VaquejadaDigital.repository;

import br.com.vaquejada_digital.VaquejadaDigital.entity.Dupla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DuplaRepository extends JpaRepository<Dupla, Long> {


    @Query("""
            SELECT d FROM Dupla d 
            WHERE (d.puxador.id = :corredorId1 AND d.esteireiro.id = :corredorId2)
               OR (d.puxador.id = :corredorId2 AND d.esteireiro.id = :corredorId1)
            """)
    Optional<Dupla> findByCorredores(Long corredorId1, Long corredorId2);

    @Query("SELECT d FROM Dupla d WHERE d.puxador.id = :corredorId OR d.esteireiro.id = :corredorId")
    List<Dupla> findByCorredor(Long corredorId);

}
