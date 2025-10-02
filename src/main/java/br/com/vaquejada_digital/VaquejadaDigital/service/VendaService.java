package br.com.vaquejada_digital.VaquejadaDigital.service;

import br.com.vaquejada_digital.VaquejadaDigital.controller.request.ComprarSenhaRequest;
import br.com.vaquejada_digital.VaquejadaDigital.entity.*;
import br.com.vaquejada_digital.VaquejadaDigital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final SenhaRepository senhaRepository;
    private final SenhaService senhaService;
    private final EventoRepository eventoRepository;
    private final DuplaRepository duplaRepository;


    @Transactional
    public Senha comprarSenha(ComprarSenhaRequest request) {

        Senha senha = senhaService.buscarSenha(
                request.eventoId(),
                request.categoriaId(),
                request.numeroSenha()
        );

        if (!senha.isDisponivel()) {
            throw new RuntimeException("Senha não está disponível");
        }

        Dupla dupla = senhaService.buscarOuCriarDupla(
                request.puxadorId(),
                request.esteireiroId(),
                request.nomeDupla()
        );

        if (request.comDesconto()) {
            senha.venderComDesconto(dupla, request.diaCorrida(), request.formaPagamento());
        } else {
            senha.venderSemDesconto(dupla, request.diaCorrida(), request.formaPagamento());
        }

        Senha senhaSalva = senhaRepository.save(senha);

        Evento evento = senha.getEvento();
        eventoRepository.save(evento);

        return senhaSalva;
    }


    public List<Senha> buscarSenhasDoCorretor(Long corredorId) {
        List<Dupla> duplas = duplaRepository.findByCorredor(corredorId);

        List<Long> duplaIds = duplas.stream()
                .map(Dupla::getId)
                .toList();

        return senhaRepository.findByDuplaIdIn(duplaIds);
    }
}