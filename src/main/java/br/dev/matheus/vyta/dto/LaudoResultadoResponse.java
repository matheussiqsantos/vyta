package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.LaudoResultado;
import br.dev.matheus.vyta.model.enums.StatusResultado;

public record LaudoResultadoResponse(
        Long id, String nome, String valor, String unidade,
        String referencia, StatusResultado status) {

    public static LaudoResultadoResponse de(LaudoResultado r) {
        return new LaudoResultadoResponse(r.getId(), r.getNome(), r.getValor(),
                r.getUnidade(), r.getReferencia(), r.getStatus());
    }
}