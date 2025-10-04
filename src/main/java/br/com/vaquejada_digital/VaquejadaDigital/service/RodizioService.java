package br.com.vaquejada_digital.VaquejadaDigital.service;

import br.com.vaquejada_digital.VaquejadaDigital.entity.*;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.StatusChamada;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.StatusPagamento;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.StatusRodizio;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.TipoRodizio;
import br.com.vaquejada_digital.VaquejadaDigital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RodizioService {

    private final RodizioRepository rodizioRepository;
    private final OrdemCorridaRepository ordemCorridaRepository;
    private final SenhaRepository senhaRepository;
    private final EventoRepository eventoRepository;
    private final CategoriaRepository categoriaRepository;


    public List<Rodizio> criarRodzioAutomatico(Long eventoId, Long categoriaId, Integer tamanhoRodizio) {

        List<Senha> senhasPagas = senhaRepository.findByEventoIdAndCategoriaIdAndStatusPagamento(
                eventoId, categoriaId, StatusPagamento.PAGO
        );

        if (senhasPagas.isEmpty()) {
            throw new RuntimeException("Não há senhas para esse rodizio");
        }

        List<List<Senha>> grupos = dividirEmGrupos(senhasPagas, tamanhoRodizio);

        List<Rodizio> rodizios = new ArrayList<>();
        int numeroRodizio = 1;

        for (List<Senha> grupo : grupos) {
            Rodizio rodizio = criarRodizio(eventoId, categoriaId, numeroRodizio);
            rodizios.add(rodizio);

            int posicao = 1;
            for (Senha senha : grupo) {
                criarOrdemCorrida(rodizio, senha, posicao);
                posicao++;
            }
            numeroRodizio++;
        }
        return rodizios;
    }

    public Rodizio atualizarStatus(Long rodizioId, StatusRodizio novoStatus) {
        Rodizio rodizio = buscarPorId(rodizioId).orElseThrow();
        rodizio.setStatus(novoStatus);
        return rodizioRepository.save(rodizio);
    }

    public Rodizio criarRodizioManual(Long eventoId, Long categoriaId, List<Long> senhaIds) {

        // 1. Buscar todas as senhas pelos IDs
        List<Senha> senhas = senhaRepository.findAllById(senhaIds);

        // 2. Validar que todas foram encontradas
        if (senhas.size() != senhaIds.size()) {
            throw new RuntimeException("Algumas senhas não foram encontradas");
        }

        // 3. Validar que todas estão PAGAS
        for (Senha senha : senhas) {
            if (senha.getStatusPagamento() != StatusPagamento.PAGO) {
                throw new RuntimeException(
                        "Senha " + senha.getNumeroSenha() + " não está paga"
                );
            }
        }

        // 4. Descobrir qual será o próximo número de rodízio
        Integer proximoNumero = buscarProximoNumeroRodizio(eventoId, categoriaId);

        // 5. Criar o rodízio (usando seu método privado)
        Rodizio rodizio = criarRodizio(eventoId, categoriaId, proximoNumero);

        // 6. Adicionar as senhas na ordem fornecida
        int posicao = 1;
        for (Long senhaId : senhaIds) {
            Senha senha = senhas.stream()
                    .filter(s -> s.getId().equals(senhaId))
                    .findFirst()
                    .orElseThrow();

            criarOrdemCorrida(rodizio, senha, posicao);
            posicao++;
        }

        return rodizio;
    }

    private List<List<Senha>> dividirEmGrupos(List<Senha> senhas, Integer tamanho) {
        List<List<Senha>> grupos = new ArrayList<>();

        for (int i = 0; i < senhas.size(); i += tamanho) {
            int fim = Math.min(i + tamanho, senhas.size());
            grupos.add(new ArrayList<>(senhas.subList(i, fim)));
        }

        return grupos;
    }


    private Rodizio criarRodizio(Long eventoId, Long categoriaId, Integer numero) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Rodizio rodizio = Rodizio.builder()
                .evento(evento)
                .categoria(categoria)
                .numeroRodizio(numero)
                .tipoRodizio(TipoRodizio.NORMAL)
                .status(StatusRodizio.AGUARDANDO)
                .criadoEm(LocalDateTime.now())
                .build();

        return rodizioRepository.save(rodizio);
    }

    private OrdemCorrida criarOrdemCorrida(Rodizio rodizio, Senha senha, Integer posicao) {
        OrdemCorrida ordem = OrdemCorrida.builder()
                .rodizio(rodizio)
                .senha(senha)
                .posicao(posicao)
                .statusChamada(StatusChamada.AGUARDANDO)
                .criadoEm(LocalDateTime.now())
                .build();

        return ordemCorridaRepository.save(ordem);
    }

    private Integer buscarProximoNumeroRodizio(Long eventoId, Long categoriaId) {
        return rodizioRepository
                .findMaxNumeroRodizioByEventoAndCategoria(eventoId, categoriaId)
                .map(num -> num + 1)  // Pega o maior e adiciona 1
                .orElse(1);           // Se não tem nenhum, começa do 1
    }

    public Optional<Rodizio> buscarPorId(Long id) {
        return rodizioRepository.findById(id);
    }

    public List<Rodizio> buscarPorEvento(Long eventoId) {
        return rodizioRepository.findByEventoIdOrderByNumeroRodizioAsc(eventoId);
    }
}