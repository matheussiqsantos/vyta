package br.dev.matheus.vyta.model.enums;

public enum TipoSanguineo {
    A_POSITIVO("A+"), A_NEGATIVO("A-"),
    B_POSITIVO("B+"), B_NEGATIVO("B-"),
    AB_POSITIVO("AB+"), AB_NEGATIVO("AB-"),
    O_POSITIVO("O+"), O_NEGATIVO("O-");
    
    private final String descricao;

    private TipoSanguineo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
    
    public static TipoSanguineo fromDescricao(String valor) {
        for (TipoSanguineo t : values()) {
            if (t.descricao.equals(valor)) return t;
        }
        throw new IllegalArgumentException("Tipo sanguíneo inválido: " + valor);
    }
}
