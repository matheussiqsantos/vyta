package br.dev.matheus.vyta.model.enums;

public enum TipoReceita {
     BRANCA_SIMPLES(30),
    BRANCA_CONTROLE_ESPECIAL(30),
    BRANCA_ANTIMICROBIANOS(10),
    AZUL(30),
    AMARELA(30);

    private final int diasValidade;

    TipoReceita(int diasValidade) {
        this.diasValidade = diasValidade;
    }

    public int getDiasValidade() {
        return diasValidade;
    }

    public boolean exigeNotificacao() {
        return this == AZUL || this == AMARELA;
    }
}
