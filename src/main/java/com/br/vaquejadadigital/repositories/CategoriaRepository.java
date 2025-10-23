package com.br.vaquejadadigital.repositories;

import com.br.vaquejadadigital.entities.Categoria;
import com.br.vaquejadadigital.entities.enums.TipoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNome(TipoCategoria nome);

    List<Categoria> findByAtivoTrue();
}
