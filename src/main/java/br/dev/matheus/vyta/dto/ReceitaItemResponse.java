package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.ReceitaItem;

public record ReceitaItemResponse(
        Long id,
        String nomeMedicamento,
        String dosagem,
        String quantidade,
        String duracaoTratamento
) {
    public static ReceitaItemResponse de(ReceitaItem i) {
        return new ReceitaItemResponse(i.getId(), i.getNomeMedicamento(),
                i.getDosagem(), i.getQuantidade(), i.getDuracaoTratamento());
    }
            
}
