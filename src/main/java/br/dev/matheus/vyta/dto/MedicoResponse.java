package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.Medico;
import br.dev.matheus.vyta.model.enums.StatusConta;

public record MedicoResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        String crm,
        String crmUf,
        String especialidade,
        StatusConta statusConta,
        EnderecoResponse endereco
) {
    public static MedicoResponse de(Medico m) {
        return new MedicoResponse(
                m.getUsuarioId(), m.getNome(), m.getEmail(), m.getTelefone(),
                m.getCrm(), m.getCrmUf(), m.getEspecialidade(), m.getStatusConta(),
                m.getEndereco() == null ? null : EnderecoResponse.de(m.getEndereco()));
    }
}
