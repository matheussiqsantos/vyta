package br.dev.matheus.vyta.repository;

import br.dev.matheus.vyta.model.Laudo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaudoRepository extends JpaRepository<Laudo, Long> {
    List<Laudo> findByPacienteUsuarioIdOrderByDataExameDesc(Long pacienteId);
    List<Laudo> findByConsultaConsultaId(Long consultaId);
}
