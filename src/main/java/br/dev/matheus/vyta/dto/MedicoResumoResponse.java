package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.Medico;

public record MedicoResumoResponse(Long id, String nome, String crm, String crmUf, String especialidade) {
    public static MedicoResumoResponse de(Medico m) {
        return new MedicoResumoResponse(m.getUsuarioId(), m.getNome(), m.getCrm(), m.getCrmUf(), m.getEspecialidade());
    }
}