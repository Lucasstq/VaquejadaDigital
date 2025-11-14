package com.br.vaquejadadigital.service;

import com.br.vaquejadadigital.entities.*;
import com.br.vaquejadadigital.entities.enums.StatusPagamento;
import com.br.vaquejadadigital.entities.enums.StatusSenha;
import com.br.vaquejadadigital.entities.enums.TipoVenda;
import com.br.vaquejadadigital.exception.BusinessException;
import com.br.vaquejadadigital.repositories.EventoRepository;
import com.br.vaquejadadigital.repositories.SenhaRepository;
import com.br.vaquejadadigital.repositories.UsuarioRepository;
import com.br.vaquejadadigital.repositories.VendaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VendaService {

    private final VendaRepository vendaRepository;
    private final EventoRepository eventoRepository;
    private final SenhaRepository senhaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MercadoPagoService mercadoPagoService;
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
        log.info("Processando venda online para evento {}", venda.getEvento().getId());

        Evento evento = eventoRepository.findById(venda.getEvento().getId())
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        venda.setEvento(evento);
        venda.setTipoVenda(TipoVenda.ONLINE);
        venda.setDataVenda(LocalDateTime.now());

        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemVenda item : itens) {
            Senha senha = senhaRepository.findById(item.getSenha().getId())
                    .orElseThrow(() -> new RuntimeException("Senha não encontrada"));

            if (senha.getStatus() != StatusSenha.DISPONIVEL) {
                throw new RuntimeException("Senha " + senha.getNumeroSenha() + " não está disponível");
            }

            // Marca como RESERVADA enquanto aguarda pagamento
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

        venda.setStatusPagamento(StatusPagamento.PENDENTE);
        venda = vendaRepository.save(venda);
        log.info("Venda {} criada. Aguardando confirmação de pagamento", venda.getId());

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


    //Confirma o pagamento e finaliza a venda, Chamado pelo webhook do Mercado Pago
    @Transactional
    public void confirmarPagamento(Long vendaId) {
        log.info("Confirmando pagamento da venda {}", vendaId);

        Venda venda = buscarPorId(vendaId);

        if (venda.getStatusPagamento() != StatusPagamento.APROVADO) {
            throw new BusinessException("Pagamento não está aprovado");
        }

        // Marca todas as senhas como VENDIDAS
        for (ItemVenda item : venda.getItens()) {
            item.getSenha().setStatus(StatusSenha.VENDIDA);
            senhaRepository.save(item.getSenha());
        }

        // Atualiza contador de senhas vendidas do evento
        Evento evento = venda.getEvento();
        evento.setQuantidadeSenhasVendidas(evento.getQuantidadeSenhasVendidas() + venda.getItens().size());
        eventoRepository.save(evento);

        log.info("Venda {} confirmada com sucesso", vendaId);
    }

    @Transactional
    public void cancelarVendaPendente(Long vendaId) {
        log.info("Cancelando venda pendente {}", vendaId);

        Venda venda = buscarPorId(vendaId);

        if (venda.getStatusPagamento() == StatusPagamento.APROVADO) {
            throw new BusinessException("Não é possível cancelar venda já aprovada");
        }

        // Libera as senhas reservadas
        for (ItemVenda item : venda.getItens()) {
            Senha senha = item.getSenha();
            senha.setStatus(StatusSenha.DISPONIVEL);
            senha.setDupla(null);
            senhaRepository.save(senha);
        }

        venda.setStatusPagamento(StatusPagamento.CANCELADO);
        vendaRepository.save(venda);

        log.info("Venda {} cancelada e senhas liberadas", vendaId);
    }

}
