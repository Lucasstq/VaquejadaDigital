package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.entities.Categoria;
import com.br.vaquejadadigital.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Transactional
    public List<Categoria> listarAtivas() {
        return categoriaRepository.findByAtivoTrue();
    }

    @Transactional
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }
}