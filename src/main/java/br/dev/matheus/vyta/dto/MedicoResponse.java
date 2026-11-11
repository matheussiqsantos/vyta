package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.Medico;
import br.dev.matheus.vyta.model.enums.SexoBiologico;
import br.dev.matheus.vyta.model.enums.StatusConta;
import java.time.LocalDate;

public record MedicoResponse(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        LocalDate dataNascimento,
        SexoBiologico sexoBiologico,
        String identidadeGenero,
        String nomeContatoEmergencia,
        String telContatoEmergencia,
        StatusConta statusConta,
        Long enderecoId,
        EnderecoResponse endereco,
        String crm,
        String crmUf,
        String especialidade
) {
    public static MedicoResponse de(Medico m) {
        return new MedicoResponse(
                m.getUsuarioId(), m.getNome(), m.getCpf(), m.getEmail(), m.getTelefone(),
                m.getDataNascimento(), m.getSexoBiologico(), m.getIdentidadeGenero(),
                m.getNomeContatoEmergencia(), m.getTelContatoEmergencia(), m.getStatusConta(),
                m.getEndereco() != null ? m.getEndereco().getEnderecoId() : null,
                m.getEndereco() != null ? EnderecoResponse.de(m.getEndereco()) : null,
                m.getCrm(), m.getCrmUf(), m.getEspecialidade());
    }
}
