package br.com.vaquejada_digital.VaquejadaDigital.service;

import br.com.vaquejada_digital.VaquejadaDigital.entity.OrdemCorrida;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Rodizio;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Usuarios;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.ResultadoCorrida;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.StatusChamada;
import br.com.vaquejada_digital.VaquejadaDigital.entity.enums.StatusRodizio;
import br.com.vaquejada_digital.VaquejadaDigital.repository.OrdemCorridaRepository;
import br.com.vaquejada_digital.VaquejadaDigital.repository.RodizioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdemCorridaService {

    private final RodizioRepository rodizioRepository;
    private final OrdemCorridaRepository ordemCorridaRepository;


    public List<OrdemCorrida> buscarPorRodizio(Long rodizioId) {
        return ordemCorridaRepository.findByRodizioIdOrderByPosicaoAsc(rodizioId);
    }


    public OrdemCorrida buscarPorId(Long id) {
        return ordemCorridaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de corrida não encontrada"));
    }

    public OrdemCorrida chamarProxima(Long rodizioId) {
        OrdemCorrida proxima = ordemCorridaRepository
                .findFirstByRodizioIdAndStatusChamadaOrderByPosicaoAsc(
                        rodizioId,
                        StatusChamada.AGUARDANDO
                )
                .orElseThrow(() -> new RuntimeException("Não há mais duplas para chamar neste rodízio"));

        proxima.setStatusChamada(StatusChamada.CHAMADO);
        proxima.setHorarioChamada(LocalDateTime.now());

        OrdemCorrida salva = ordemCorridaRepository.save(proxima);

        // TODO: Enviar notificação para o corredor
        // notificacaoService.notificarChamada(proxima.getSenha().getDupla());

        Rodizio rodizio = proxima.getRodizio();
        if (rodizio.getStatus() == StatusRodizio.AGUARDANDO) {
            rodizio.setStatus(StatusRodizio.EM_ANDAMENTO);
            rodizioRepository.save(rodizio);
        }

        return salva;
    }

    public OrdemCorrida marcarFalta(Long ordemCorridaId) {
        OrdemCorrida ordem = buscarPorId(ordemCorridaId);

        if (ordem.getStatusChamada() != StatusChamada.CHAMADO) {
            throw new IllegalStateException("Só é possível marcar falta para duplas que foram chamadas");
        }

        ordem.setStatusChamada(StatusChamada.FALTA);
        ordem.setResultado(ResultadoCorrida.FALTA);

        OrdemCorrida salva = ordemCorridaRepository.save(ordem);

        // TODO: Adicionar ao Rabo da Gata automaticamente
        // raboGataService.adicionarAoRaboGata(ordem);

        return salva;
    }

    public OrdemCorrida alterarPosicao(Long ordemCorridaId, Integer novaPosicao) {
        OrdemCorrida ordem = buscarPorId(ordemCorridaId);

        if (ordem.getStatusChamada() == StatusChamada.CORREU) {
            throw new IllegalStateException("Não é possível alterar a posição de uma dupla que já correu");
        }

        Integer posicaoAtual = ordem.getPosicao();
        Long rodizioId = ordem.getRodizio().getId();

        List<OrdemCorrida> todasOrdens = buscarPorRodizio(rodizioId);

        if (novaPosicao < 1 || novaPosicao > todasOrdens.size()) {
            throw new IllegalArgumentException("Posição inválida");
        }

        if (novaPosicao < posicaoAtual) {
            for (OrdemCorrida o : todasOrdens) {
                if (o.getPosicao() >= novaPosicao && o.getPosicao() < posicaoAtual) {
                    o.setPosicao(o.getPosicao() + 1);
                    ordemCorridaRepository.save(o);
                }
            }
        } else if (novaPosicao > posicaoAtual) {
            for (OrdemCorrida o : todasOrdens) {
                if (o.getPosicao() > posicaoAtual && o.getPosicao() <= novaPosicao) {
                    o.setPosicao(o.getPosicao() - 1);
                    ordemCorridaRepository.save(o);
                }
            }
        }

        ordem.setPosicao(novaPosicao);
        return ordemCorridaRepository.save(ordem);
    }

    public OrdemCorrida registrarResultado(
            Long ordemCorridaId,
            ResultadoCorrida resultado,
            Usuarios juiz) {

        OrdemCorrida ordem = buscarPorId(ordemCorridaId);

        if (ordem.getStatusChamada() != StatusChamada.CHAMADO) {
            throw new IllegalStateException("A dupla precisa ter sido chamada antes de registrar resultado");
        }

        // Registrar o resultado
        ordem.setResultado(resultado);
        ordem.setStatusChamada(StatusChamada.CORREU);
        ordem.setHorarioCorrida(LocalDateTime.now());
        ordem.setJuiz(juiz);

        OrdemCorrida salva = ordemCorridaRepository.save(ordem);

        // Ações específicas por tipo de resultado
        switch (resultado) {
            case RETORNO:
                // TODO: Adicionar à lista de retornos
                // retornoService.adicionarAoRetorno(ordem);
                break;
            case FALTA:
                // TODO: Adicionar ao Rabo da Gata
                // raboGataService.adicionarAoRaboGata(ordem);
                break;
            case VALEU_O_BOI:
            case ZERO:
            case DESCLASSIFICADO:
            case CANCELADO:
                break;
        }

        verificarSeRodizioFinalizou(ordem.getRodizio().getId());

        return salva;
    }

    public OrdemCorrida atualizarObservacoes(Long ordemCorridaId, String observacoes) {
        OrdemCorrida ordem = buscarPorId(ordemCorridaId);
        ordem.setObservacoes(observacoes);
        return ordemCorridaRepository.save(ordem);
    }


    private void verificarSeRodizioFinalizou(Long rodizioId) {
        List<OrdemCorrida> ordens = buscarPorRodizio(rodizioId);

        boolean todasCorreram = ordens.stream()
                .allMatch(o -> o.getStatusChamada() == StatusChamada.CORREU ||
                        o.getStatusChamada() == StatusChamada.FALTA);

        if (todasCorreram) {
            Rodizio rodizio = rodizioRepository.findById(rodizioId)
                    .orElseThrow();
            rodizio.setStatus(StatusRodizio.FINALIZADO);
            rodizioRepository.save(rodizio);
        }
    }

}
