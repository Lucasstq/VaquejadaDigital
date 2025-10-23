package com.br.vaquejadadigital.mapper;

import com.br.vaquejadadigital.dtos.request.ItemVendaRequest;
import com.br.vaquejadadigital.dtos.request.VendaManualRequest;
import com.br.vaquejadadigital.dtos.request.VendaOnlineRequest;
import com.br.vaquejadadigital.dtos.response.ItemVendaResponse;
import com.br.vaquejadadigital.dtos.response.VendaResponse;
import com.br.vaquejadadigital.entities.*;
import com.br.vaquejadadigital.entities.enums.StatusPagamento;
import com.br.vaquejadadigital.entities.enums.StatusSenha;
import com.br.vaquejadadigital.repositories.EventoRepository;
import com.br.vaquejadadigital.repositories.SenhaRepository;
import com.br.vaquejadadigital.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VendaMapper {

    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SenhaRepository senhaRepository;

    private Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();

            return usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));
        }

        throw new RuntimeException("Usuário não autenticado");
    }

    public Venda toVenda(VendaManualRequest request) {
        Evento evento = eventoRepository.findById(request.eventoId())
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        // Pega o vendedor do contexto de segurança
        Usuario vendedor = getUsuarioLogado();

        // Calcula o valor total baseado nos itens
        BigDecimal valorTotal = request.itens().stream()
                .map(ItemVendaRequest::valor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Venda.builder()
                .evento(evento)
                .comprador(null) // Será definido no Service baseado na dupla
                .vendedor(vendedor)
                .formaPagamento(request.formaPagamento())
                .statusPagamento(StatusPagamento.PENDENTE)
                .valorTotal(valorTotal)
                .observacao(request.observacao())
                .dataVenda(LocalDateTime.now())
                .build();
    }


    public Venda toVenda(VendaOnlineRequest request) {
        Evento evento = eventoRepository.findById(request.eventoId())
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        Usuario comprador = usuarioRepository.findById(request.compradorId())
                .orElseThrow(() -> new RuntimeException("Comprador não encontrado"));

        return Venda.builder()
                .evento(evento)
                .comprador(comprador)
                .vendedor(null)
                .formaPagamento(request.formaPagamento())
                .statusPagamento(StatusPagamento.PENDENTE)
                .valorTotal(request.valorTotal())
                .observacao("Venda online")
                .dataVenda(LocalDateTime.now())
                .build();
    }


    public ItemVenda toItemVenda(ItemVendaRequest request, Dupla dupla) {
        Senha senha = senhaRepository.findById(request.senhaId())
                .orElseThrow(() -> new RuntimeException("Senha não encontrada"));

        // Validar se senha está disponível
        if (senha.getStatus() != StatusSenha.DISPONIVEL) {
            throw new RuntimeException("Senha não está disponível para venda");
        }

        return ItemVenda.builder()
                .senha(senha)
                .valor(request.valor())
                .build();
    }


    public VendaResponse toResponse(Venda venda) {
        List<ItemVendaResponse> itens = venda.getItens() != null ?
                venda.getItens().stream()
                        .map(this::toItemResponse)
                        .collect(Collectors.toList())
                : List.of();

        return new VendaResponse(
                venda.getId(),
                venda.getEvento().getId(),
                venda.getEvento().getNome(),
                venda.getComprador() != null ? venda.getComprador().getId() : null,
                venda.getComprador() != null ? venda.getComprador().getNome() : null,
                venda.getVendedor() != null ? venda.getVendedor().getId() : null,
                venda.getVendedor() != null ? venda.getVendedor().getNome() : null,
                venda.getFormaPagamento(),
                venda.getStatusPagamento(),
                venda.getValorTotal(),
                venda.getObservacao(),
                venda.getDataVenda(),
                itens
        );
    }

    private ItemVendaResponse toItemResponse(ItemVenda item) {
        return new ItemVendaResponse(
                item.getId(),
                item.getSenha().getId(),
                item.getSenha().getNumeroSenha(),
                item.getSenha().getCategoria() != null ?
                        item.getSenha().getCategoria().getNome().getDescricao() : null,
                item.getSenha().getDupla() != null ?
                        item.getSenha().getDupla().getPuxador().getUsuario().getNome() : null,
                item.getSenha().getDupla() != null ?
                        item.getSenha().getDupla().getEsteireiro().getUsuario().getNome() : null,
                item.getValor()
        );
    }
}
