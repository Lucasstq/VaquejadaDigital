package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.entities.*;
import com.br.vaquejadadigital.entities.enums.StatusPagamento;
import com.br.vaquejadadigital.entities.enums.StatusSenha;
import com.br.vaquejadadigital.entities.enums.TipoVenda;
import com.br.vaquejadadigital.repositories.EventoRepository;
import com.br.vaquejadadigital.repositories.SenhaRepository;
import com.br.vaquejadadigital.repositories.UsuarioRepository;
import com.br.vaquejadadigital.repositories.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final EventoRepository eventoRepository;
    private final SenhaRepository senhaRepository;
    private final UsuarioRepository usuarioRepository;
    private final DuplaService duplaService;

    @Transactional
    public Venda vendaManual(Venda venda, List<ItemVenda> itens) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario vendedor = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Evento evento = eventoRepository.findById(venda.getEvento().getId())
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        venda.setEvento(evento);
        venda.setStatusPagamento(StatusPagamento.APROVADO);
        venda.setTipoVenda(TipoVenda.MANUAL);
        venda.setVendedor(vendedor);
        venda.setItens(new ArrayList<>());

        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemVenda item : itens) {
            Senha senha = senhaRepository.findById(item.getSenha().getId())
                    .orElseThrow(() -> new RuntimeException("Senha não encontrada"));

            if (senha.getStatus() != StatusSenha.DISPONIVEL) {
                throw new RuntimeException("Senha " + senha.getNumeroSenha() + " não está disponível");
            }

            senha.setStatus(StatusSenha.VENDIDA);
            senha.setDupla(item.getSenha().getDupla());
            senhaRepository.save(senha);

            item.setVenda(venda);
            item.setSenha(senha);
            item.setValor(senha.getValor());

            venda.getItens().add(item);
            valorTotal = valorTotal.add(senha.getValor());
        }

        venda.setValorTotal(valorTotal);
        venda = vendaRepository.save(venda);

        evento.setQuantidadeSenhasVendidas(evento.getQuantidadeSenhasVendidas() + itens.size());
        eventoRepository.save(evento);

        return venda;
    }

    @Transactional
    public Venda vendaOnline(Venda venda, List<ItemVenda> itens) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario comprador = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Evento evento = eventoRepository.findById(venda.getEvento().getId())
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        venda.setEvento(evento);
        venda.setComprador(comprador);
        venda.setStatusPagamento(StatusPagamento.PENDENTE);
        venda.setTipoVenda(TipoVenda.ONLINE);
        venda.setItens(new ArrayList<>());

        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemVenda item : itens) {
            Senha senha = senhaRepository.findById(item.getSenha().getId())
                    .orElseThrow(() -> new RuntimeException("Senha não encontrada"));

            if (senha.getStatus() != StatusSenha.DISPONIVEL) {
                throw new RuntimeException("Senha " + senha.getNumeroSenha() + " não está disponível");
            }

            senha.setStatus(StatusSenha.RESERVADA);
            senha.setDupla(item.getSenha().getDupla());
            senhaRepository.save(senha);

            item.setVenda(venda);
            item.setSenha(senha);
            item.setValor(senha.getValor());

            venda.getItens().add(item);
            valorTotal = valorTotal.add(senha.getValor());
        }

        venda.setValorTotal(valorTotal);
        venda = vendaRepository.save(venda);

        // TODO: Integrar com gateway de pagamento

        // Por enquanto, aprovar automaticamente
        venda.setStatusPagamento(StatusPagamento.APROVADO);

        for (ItemVenda item : venda.getItens()) {
            item.getSenha().setStatus(StatusSenha.VENDIDA);
        }

        evento.setQuantidadeSenhasVendidas(evento.getQuantidadeSenhasVendidas() + itens.size());
        eventoRepository.save(evento);

        return venda;
    }

    @Transactional(readOnly = true)
    public List<Venda> listarPorEvento(Long eventoId) {
        return vendaRepository.findByEventoId(eventoId);
    }

    @Transactional(readOnly = true)
    public List<Venda> listarPorComprador(Long compradorId) {
        return vendaRepository.findByCompradorId(compradorId);
    }

    @Transactional(readOnly = true)
    public Venda buscarPorId(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));
    }
}
