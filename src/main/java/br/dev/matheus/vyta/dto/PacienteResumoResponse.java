package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.Paciente;
import br.dev.matheus.vyta.model.enums.SexoBiologico;
import br.dev.matheus.vyta.model.enums.TipoSanguineo;
import java.time.LocalDate;

public record PacienteResumoResponse(
        Long id,
        String nome,
        LocalDate dataNascimento,
        SexoBiologico sexoBiologico,
        TipoSanguineo tipoSanguineo
) {
    public static PacienteResumoResponse de(Paciente p) {
        return new PacienteResumoResponse(p.getUsuarioId(), p.getNome(), p.getDataNascimento(),
                p.getSexoBiologico(), p.getTipoSanguineo());
    }
}