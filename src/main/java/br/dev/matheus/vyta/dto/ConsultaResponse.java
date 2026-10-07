package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.Consulta;
import br.dev.matheus.vyta.model.enums.ModalidadeConsulta;
import br.dev.matheus.vyta.model.enums.StatusConsulta;
import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultaResponse(
        Long id,
        Long pacienteId,
        String pacienteNome,
        Long medicoId,
        String medicoNome,
        String especialidade,
        LocalDate dataConsulta,
        LocalTime horario,
        String tipoConsulta,
        ModalidadeConsulta modalidade,
        StatusConsulta statusConsulta,
        String convenio,
        EnderecoResponse endereco
) {
    public static ConsultaResponse de(Consulta c) {
        return new ConsultaResponse(
                c.getConsultaId(),
                c.getPaciente().getUsuarioId(), c.getPaciente().getNome(),
                c.getMedico().getUsuarioId(), c.getMedico().getNome(),
                c.getMedico().getEspecialidade(),
                c.getDataConsulta(), c.getHorario(), c.getTipoConsulta(),
                c.getModalidade(), c.getStatusConsulta(),
                c.getConvenio(),
                c.getEndereco() == null ? null : EnderecoResponse.de(c.getEndereco()));
    }
}
