package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.Medico;
import br.dev.matheus.vyta.model.enums.StatusConta;

public record MedicoResumoResponse(
        Long id,
        String nome,
        String crm,
        String crmUf,
        String especialidade,
        StatusConta statusConta,
        Long enderecoId,
        EnderecoResponse endereco
) {
    public static MedicoResumoResponse de(Medico m) {
        return new MedicoResumoResponse(
                m.getUsuarioId(), m.getNome(), m.getCrm(), m.getCrmUf(), m.getEspecialidade(),
                m.getStatusConta(),
                m.getEndereco() != null ? m.getEndereco().getEnderecoId() : null,
                m.getEndereco() != null ? EnderecoResponse.de(m.getEndereco()) : null);
    }
}