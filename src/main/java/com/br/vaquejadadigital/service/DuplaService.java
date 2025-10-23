package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.entities.Corredor;
import com.br.vaquejadadigital.entities.Dupla;
import com.br.vaquejadadigital.repositories.CorredorRepository;
import com.br.vaquejadadigital.repositories.DuplaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DuplaService {

    private final DuplaRepository duplaRepository;
    private final CorredorRepository corredorRepository;

    @Transactional
    public Dupla criar(Dupla dupla) {
        if (dupla.getPuxador().getId().equals(dupla.getEsteireiro().getId())) {
            throw new RuntimeException("Puxador e esteireiro não podem ser a mesma pessoa");
        }

        Corredor puxador = corredorRepository.findById(dupla.getPuxador().getId())
                .orElseThrow(() -> new RuntimeException("Puxador não encontrado"));

        Corredor esteireiro = corredorRepository.findById(dupla.getEsteireiro().getId())
                .orElseThrow(() -> new RuntimeException("Esteireiro não encontrado"));

        // Verificar se dupla já existe
        Optional<Dupla> duplaExistente = duplaRepository.findByPuxadorAndEsteireiro(
                dupla.getPuxador().getId(), dupla.getEsteireiro().getId());

        if (duplaExistente.isPresent()) {
            return duplaExistente.get();
        }

        dupla.setPuxador(puxador);
        dupla.setEsteireiro(esteireiro);

        return duplaRepository.save(dupla);
    }

    @Transactional
    public Dupla buscarPorId(Long id) {
        return duplaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dupla não encontrada"));
    }

    @Transactional
    public List<Dupla> listarTodas() {
        return duplaRepository.findAll();
    }

    @Transactional
    public List<Dupla> listarPorCorredor(Long corredorId) {
        List<Dupla> comoPuxador = duplaRepository.findByPuxadorId(corredorId);
        List<Dupla> comoEsteireiro = duplaRepository.findByEsteireiroId(corredorId);

        comoPuxador.addAll(comoEsteireiro);

        return comoPuxador.stream()
                .distinct()
                .toList();
    }

    @Transactional
    public Dupla buscarOuCriarDupla(Long puxadorId, Long esteireiroId) {
        return duplaRepository.findByPuxadorAndEsteireiro(puxadorId, esteireiroId)
                .orElseGet(() -> {
                    Corredor puxador = corredorRepository.findById(puxadorId)
                            .orElseThrow(() -> new RuntimeException("Puxador não encontrado"));
                    Corredor esteireiro = corredorRepository.findById(esteireiroId)
                            .orElseThrow(() -> new RuntimeException("Esteireiro não encontrado"));

                    Dupla novaDupla = Dupla.builder()
                            .puxador(puxador)
                            .esteireiro(esteireiro)
                            .build();

                    return duplaRepository.save(novaDupla);
                });
    }
}
