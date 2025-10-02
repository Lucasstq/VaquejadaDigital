package br.com.vaquejada_digital.VaquejadaDigital.service;

import br.com.vaquejada_digital.VaquejadaDigital.entity.*;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.StatusPagamento;
import br.com.vaquejada_digital.VaquejadaDigital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SenhaService {

    private final SenhaRepository senhaRepository;
    private final DuplaRepository duplaRepository;
    private final CorredorRepository corredorRepository;


    @Transactional
    public void gerarSenhasParaEvento(Evento evento, Categoria categoria) {
        List<Senha> senhas = IntStream.rangeClosed(1, evento.getQuantidadeTotalSenhas())
                .mapToObj(numero -> Senha.builder()
                        .evento(evento)
                        .categoria(categoria)
                        .numeroSenha(numero)
                        .statusPagamento(StatusPagamento.PENDENTE)
                        .bloqueada(false)
                        .build())
                .toList();

        senhaRepository.saveAll(senhas);
    }


    public Senha buscarSenha(Long eventoId, Long categoriaId, Integer numeroSenha) {
        return senhaRepository.findByEventoIdAndCategoriaIdAndNumeroSenha(
                eventoId, categoriaId, numeroSenha
        ).orElseThrow(() -> new RuntimeException("Senha não encontrada"));
    }


    @Transactional
    public Dupla buscarOuCriarDupla(Long puxadorId, Long esteireiroId, String nomeDupla) {
        Optional<Dupla> existente = duplaRepository.findByCorredores(puxadorId, esteireiroId);
        if (existente.isPresent()) {
            return existente.get();
        }

        Corredor puxador = corredorRepository.findById(puxadorId)
                .orElseThrow(() -> new RuntimeException("Puxador não encontrado"));

        Corredor esteireiro = corredorRepository.findById(esteireiroId)
                .orElseThrow(() -> new RuntimeException("Esteireiro não encontrado"));

        Dupla novaDupla = Dupla.builder()
                .puxador(puxador)
                .esteireiro(esteireiro)
                .nomeDupla(nomeDupla)
                .criadoEm(LocalDateTime.now())
                .build();

        return duplaRepository.save(novaDupla);
    }


    public List<Senha> listarSenhasDoEvento(Long eventoId) {
        return senhaRepository.findByEventoId(eventoId);
    }
}

