package br.dev.matheus.vyta.model.enums;

public enum TipoReceita {
    BRANCA_SIMPLES,
    BRANCA_CONTROLE_ESPECIAL,
    BRANCA_ANTIMICROBIANOS,
    AZUL,
    AMARELA;
    
    public boolean exigeNotificacao() {
        return this == AZUL || this == AMARELA;
    }
}
