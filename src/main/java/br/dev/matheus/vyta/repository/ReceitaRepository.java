package br.dev.matheus.vyta.repository;

import br.dev.matheus.vyta.model.Receita;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    List<Receita> findByConsultaOrderByDataEmissaoDesc(Long consultaId);
}
