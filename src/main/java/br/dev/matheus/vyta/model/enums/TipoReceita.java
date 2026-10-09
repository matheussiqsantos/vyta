package br.dev.matheus.vyta.model.enums;

public enum TipoReceita {
    SIMPLES,
    CONTROLE_ESPECIAL,
    AZUL,
    AMARELA;
    
    public boolean exigeNotificacao() {
        return this == AZUL || this == AMARELA;
    }
}
