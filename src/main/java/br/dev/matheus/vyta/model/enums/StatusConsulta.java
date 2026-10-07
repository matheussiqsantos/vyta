package br.dev.matheus.vyta.model.enums;

public enum StatusConsulta {
    AGENDADA, CONFIRMADA, REALIZADA, CANCELADA;
    
    public boolean podeMudarPara(StatusConsulta novo) {
        return switch(this) {
            case AGENDADA -> novo == CONFIRMADA || novo == CANCELADA;
            case CONFIRMADA -> novo == REALIZADA || novo == CANCELADA;
            case REALIZADA, CANCELADA -> false;
        };
    }
}
