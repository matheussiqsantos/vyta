package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.Receita;
import br.dev.matheus.vyta.model.enums.StatusReceita;
import br.dev.matheus.vyta.model.enums.TipoReceita;
import java.time.LocalDate;
import java.util.List;

public record ReceitaResponse(
    Long id,
    Long consultaId,
    LocalDate dataEmissao,
    LocalDate dataVencimento,
    TipoReceita tipoReceita,
    StatusReceita statusReceita,
    String numeroNotificaca,
    List<ReceitaItemResponse> itens
) {
    public static ReceitaResponse de(Receita r) {
        return new ReceitaResponse(
                r.getId(), r.getConsulta().getConsultaId(),
                r.getDataEmissao(), r.getDataVencimento(),
                r.getTipoReceita(), r.getStatusReceita(), r.getNumeroNotificacao(),
                r.getItens().stream().map(ReceitaItemResponse::de).toList());
    }
}
