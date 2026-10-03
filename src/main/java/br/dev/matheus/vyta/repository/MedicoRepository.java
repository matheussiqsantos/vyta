package br.dev.matheus.vyta.repository;

import br.dev.matheus.vyta.model.Medico;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    
    Optional<Medico> findByEmail(String email);
    
    Optional<Medico> findByCrmAndCrmUf(String crm, String crmUf);
    
    boolean existsByCrmAndCrmUf(String crm, String crmUf);
    
    List<Medico> findByEspecialidadeIgnoreCase(String especialidade);
}
