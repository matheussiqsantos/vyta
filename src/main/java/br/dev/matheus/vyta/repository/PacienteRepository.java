package br.dev.matheus.vyta.repository;

import br.dev.matheus.vyta.model.Paciente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
    Optional<Paciente> findByEmail(String email);
    
    Optional<Paciente> findByCpf(String cpf);
}