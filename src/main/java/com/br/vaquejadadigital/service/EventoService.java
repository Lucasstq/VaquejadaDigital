package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.entities.Categoria;
import com.br.vaquejadadigital.entities.Evento;
import com.br.vaquejadadigital.entities.Senha;
import com.br.vaquejadadigital.entities.enums.StatusEvento;
import com.br.vaquejadadigital.entities.enums.StatusSenha;
import com.br.vaquejadadigital.exception.NoCategoriesAvailableException;
import com.br.vaquejadadigital.repositories.CategoriaRepository;
import com.br.vaquejadadigital.repositories.EventoRepository;
import com.br.vaquejadadigital.repositories.SenhaRepository;
import com.br.vaquejadadigital.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final CategoriaRepository categoriaRepository;
    private final SenhaRepository senhaRepository;

    @Transactional
    public Evento criarEvento(Evento evento) {

        if (evento.getDataFim().isBefore(evento.getDataInicio())) {
            throw new RuntimeException("Data de fim deve ser posterior à data de início");
        }

        List<Categoria> categorias = categoriaRepository.findByAtivoTrue();
        if (categorias.isEmpty()) {
            throw new NoCategoriesAvailableException(
                    "Não há categorias ativas para criar senhas. Ative pelo menos uma categoria antes de criar um evento."
            );
        }

        evento.setQuantidadeSenhasVendidas(0);
        evento.setStatus(StatusEvento.CRIADO);
        evento = eventoRepository.save(evento);

        List<Senha> senhas = criarSenhasParaEvento(evento, categorias);
        senhaRepository.saveAll(senhas);

        return evento;
    }


    private List<Senha> criarSenhasParaEvento(Evento evento, List<Categoria> categorias) {
        List<Senha> senhas = new ArrayList<>();

        int senhasPorCategoria = evento.getQuantidadeSenhasTotal() / categorias.size();
        int senhasRestantes = evento.getQuantidadeSenhasTotal() % categorias.size();

        int numeroSenhaAtual = 1;

        for (int i = 0; i < categorias.size(); i++) {
            Categoria categoria = categorias.get(i);
            int quantidadeParaEstaCategoria = senhasPorCategoria;

            // Distribuir senhas restantes nas primeiras categorias
            if (i < senhasRestantes) {
                quantidadeParaEstaCategoria++;
            }

            for (int j = 0; j < quantidadeParaEstaCategoria; j++) {
                senhas.add(Senha.builder()
                        .evento(evento)
                        .categoria(categoria)
                        .numeroSenha(numeroSenhaAtual++)
                        .status(StatusSenha.DISPONIVEL)
                        .valor(evento.getValorSenha())
                        .build());
            }
        }

        return senhas;
    }

    @Transactional
    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
    }

    @Transactional
    public List<Evento> listarEventosAtivos() {
        return eventoRepository.findEventosAtivos();
    }

    @Transactional
    public Evento buscarPorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
    }

    @Transactional
    public Evento atualizarEvento(Long id, Evento eventoAtualizado) {
        Evento evento = buscarPorId(id);

        if (eventoAtualizado.getNome() != null) evento.setNome(eventoAtualizado.getNome());
        if (eventoAtualizado.getDescricao() != null) evento.setDescricao(eventoAtualizado.getDescricao());
        if (eventoAtualizado.getLocal() != null) evento.setLocal(eventoAtualizado.getLocal());
        if (eventoAtualizado.getDataInicio() != null) evento.setDataInicio(eventoAtualizado.getDataInicio());
        if (eventoAtualizado.getDataFim() != null) evento.setDataFim(eventoAtualizado.getDataFim());
        if (eventoAtualizado.getValorSenha() != null) evento.setValorSenha(eventoAtualizado.getValorSenha());
        if (eventoAtualizado.getLimiteDuplasPorRodizio() != null) evento.setLimiteDuplasPorRodizio(eventoAtualizado.getLimiteDuplasPorRodizio());
        if (eventoAtualizado.getStatus() != null) evento.setStatus(eventoAtualizado.getStatus());

        return eventoRepository.save(evento);
    }

    @Transactional
    public void deletarEvento(Long id) {
        Evento evento = buscarPorId(id);

        if (evento.getQuantidadeSenhasVendidas() > 0) {
            throw new RuntimeException("Não é possível deletar evento com senhas vendidas");
        }

        eventoRepository.delete(evento);
    }
}
