package br.com.vaquejada_digital.VaquejadaDigital.mapper;

import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.EventoResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.reponse.UsuarioSimpleResponse;
import br.com.vaquejada_digital.VaquejadaDigital.controller.request.EventoRequest;
import br.com.vaquejada_digital.VaquejadaDigital.entity.Evento;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.List;

@UtilityClass
public class EventoMapper {
    public static Evento toEvent(EventoRequest eventRequest) {

        return Evento
                .builder()
                .nome(eventRequest.nome())
                .dataInicio(eventRequest.dataInicio())
                .dataFim(eventRequest.dataFim())
                .local(eventRequest.local())
                .descricao(eventRequest.descricao())
                .precoBaseSenha(eventRequest.precoBaseSenha())
                .precoComDesconto(eventRequest.precoComDesconto())
                .percentualDesconto(eventRequest.percentualDesconto())
                .valorAbvaq(eventRequest.valorAbvaq() != null ?
                        eventRequest.valorAbvaq() :
                        new BigDecimal("10.00"))
                .quantidadeTotalSenhas(eventRequest.quantidadeTotalSenhas())
                .senhasVendidas(0)
                .imagensVideos(eventRequest.imagensVideos())
                .build();
    }

    public static EventoResponse toEventResponse(Evento event) {

        List<UsuarioSimpleResponse> juizesList = event.getJuizes().stream()
                .map(UsuariosMapper::toUsuarioSimpleResponse)
                .toList();

        List<UsuarioSimpleResponse> locutoresList = event.getLocutores().stream()
                .map(UsuariosMapper::toUsuarioSimpleResponse)
                .toList();

        return EventoResponse
                .builder()
                .id(event.getId())
                .nome(event.getNome())
                .local(event.getLocal())
                .descricao(event.getDescricao())
                .precoBaseSenha(event.getPrecoBaseSenha())
                .dataInicio(event.getDataInicio())
                .dataFim(event.getDataFim())
                .imagensVideos(event.getImagensVideos())
                .quantidadeTotalSenhas(event.getQuantidadeTotalSenhas())
                .status(event.getStatus())
                .juizes(juizesList)
                .locutores(locutoresList)
                .senhasVendidas(event.getSenhasVendidas())
                .senhasDisponiveis(event.getSenhasDisponiveis())
                .precoComDesconto(event.getPrecoComDesconto())
                .valorAbvaq(event.getValorAbvaq())
                .percentualDesconto(event.getPercentualDesconto())
                .build();
    }
}
