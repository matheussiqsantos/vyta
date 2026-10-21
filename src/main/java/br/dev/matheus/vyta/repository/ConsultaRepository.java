package br.dev.matheus.vyta.repository;

import br.dev.matheus.vyta.model.Consulta;
import br.dev.matheus.vyta.model.enums.StatusConsulta;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByPacienteUsuarioIdOrderByDataConsultaAscHorarioAsc(Long pacienteId);
    List<Consulta> findByMedicoUsuarioIdOrderByDataConsultaAscHorarioAsc(Long medicoId);
    
    boolean existsByMedicoUsuarioIdAndDataConsultaAndHorarioAndStatusConsultaNot(
            Long medicoId, LocalDate data, LocalTime horario, StatusConsulta status);
    
boolean existsByPacienteUsuarioIdAndDataConsultaAndHorarioAndStatusConsultaNot(
            Long pacienteId, LocalDate data, LocalTime horario, StatusConsulta status);    

@Query("""
    select count(c) > 0 from Consulta c
    where c.medico.usuarioId = :id
      and c.statusConsulta in :status
      and (c.dataConsulta > :hoje or (c.dataConsulta = :hoje and c.horario > :agora))
""")
boolean medicoTemConsultaFutura(@Param("id") Long id,
        @Param("status") Collection<StatusConsulta> status,
        @Param("hoje") LocalDate hoje, @Param("agora") LocalTime agora);

@Query("""
    select count(c) > 0 from Consulta c
    where c.paciente.usuarioId = :id
      and c.statusConsulta in :status
      and (c.dataConsulta > :hoje or (c.dataConsulta = :hoje and c.horario > :agora))
""")
boolean pacienteTemConsultaFutura(@Param("id") Long id,
        @Param("status") Collection<StatusConsulta> status,
        @Param("hoje") LocalDate hoje, @Param("agora") LocalTime agora);
}
