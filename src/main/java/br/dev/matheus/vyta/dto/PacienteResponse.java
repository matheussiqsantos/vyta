package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.Paciente;
import br.dev.matheus.vyta.model.enums.StatusConta;
import br.dev.matheus.vyta.model.enums.TipoSanguineo;

import java.time.LocalDate;

public record PacienteResponse(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        LocalDate dataNascimento,
        TipoSanguineo tipoSanguineo,
        StatusConta statusConta,
        EnderecoResponse endereco
) {
    public static PacienteResponse de(Paciente p) {
        return new PacienteResponse(
                p.getUsuarioId(), p.getNome(), p.getCpf(), p.getEmail(),
                p.getTelefone(), p.getDataNascimento(),
                p.getTipoSanguineo(), p.getStatusConta(),
                p.getEndereco() == null ? null : EnderecoResponse.de(p.getEndereco()));
    }
}
