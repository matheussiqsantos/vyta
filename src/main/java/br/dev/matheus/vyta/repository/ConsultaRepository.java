package br.dev.matheus.vyta.repository;

import br.dev.matheus.vyta.model.Consulta;
import br.dev.matheus.vyta.model.enums.StatusConsulta;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByPacienteUsuarioIdOrderByDataConsultaAscHorarioAsc(Long pacienteId);
    List<Consulta> findByMedicoUsuarioIdOrderByDataConsultaAscHorarioAsc(Long medicoId);
    
    boolean existsByMedicoUsuarioIdAndDataConsultaAndHorarioAndStatusConsultaNot(
            Long medicoId, LocalDate data, LocalTime horario, StatusConsulta status);
    
boolean existsByPacienteUsuarioIdAndDataConsultaAndHorarioAndStatusConsultaNot(
            Long pacienteId, LocalDate data, LocalTime horario, StatusConsulta status);    
}
