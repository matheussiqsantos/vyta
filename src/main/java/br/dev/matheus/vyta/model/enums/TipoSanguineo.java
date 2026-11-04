package br.dev.matheus.vyta.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoSanguineo {
    A_POSITIVO("A+"), A_NEGATIVO("A-"),
    B_POSITIVO("B+"), B_NEGATIVO("B-"),
    AB_POSITIVO("AB+"), AB_NEGATIVO("AB-"),
    O_POSITIVO("O+"), O_NEGATIVO("O-");

    private final String simbolo;

    TipoSanguineo(String simbolo) {
        this.simbolo = simbolo;
    }

    @JsonValue
    public String getSimbolo() {
        return simbolo;
    }

    @JsonCreator
    public static TipoSanguineo fromSimbolo(String valor) {
        if (valor == null || valor.isBlank()) return null;
        String v = valor.trim().toUpperCase();
        for (TipoSanguineo t : values()) {
            if (t.simbolo.equals(v) || t.name().equals(v)) return t;
        }
        throw new IllegalArgumentException("Tipo sanguíneo inválido: " + valor);
    }
}