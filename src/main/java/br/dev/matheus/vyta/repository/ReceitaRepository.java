package br.dev.matheus.vyta.repository;

import br.dev.matheus.vyta.model.Consulta;
import br.dev.matheus.vyta.model.Receita;
import br.dev.matheus.vyta.model.enums.StatusReceita;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    List<Receita> findByConsultaPacienteUsuarioIdOrderByDataEmissaoDesc(Long pacienteId);
    List<Receita> findByConsultaConsultaIdOrderByDataEmissaoDesc(Long consultaId);

    @Modifying
    @Query("""
        update Receita r set r.statusReceita = :vencida
        where r.statusReceita = :valida and r.dataVencimento < :hoje
    """)
    int marcarVencidas(@Param("valida") StatusReceita valida,
                       @Param("vencida") StatusReceita vencida,
                       @Param("hoje") LocalDate hoje);
}
