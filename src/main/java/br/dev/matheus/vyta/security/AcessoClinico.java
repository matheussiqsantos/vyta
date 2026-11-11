package br.dev.matheus.vyta.security;

import br.dev.matheus.vyta.exception.RecursoNaoEncontradoException;
import br.dev.matheus.vyta.model.Consulta;
import br.dev.matheus.vyta.model.Laudo;
import br.dev.matheus.vyta.model.Receita;
import br.dev.matheus.vyta.repository.ConsultaRepository;
import br.dev.matheus.vyta.repository.LaudoRepository;
import br.dev.matheus.vyta.repository.ReceitaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Regras de acesso aos dados clínicos (consultas, laudos e receitas).
 *
 * - PACIENTE: só enxerga os próprios dados.
 * - MEDICO: só enxerga dados de pacientes com quem tem (ou teve) consulta,
 *   e só altera o que pertence às suas próprias consultas.
 */
@Component
public class AcessoClinico {

    private static final String MEDICO = "MEDICO";
    private static final String PACIENTE = "PACIENTE";

    private final ConsultaRepository consultaRepository;
    private final LaudoRepository laudoRepository;
    private final ReceitaRepository receitaRepository;

    public AcessoClinico(ConsultaRepository consultaRepository,
                         LaudoRepository laudoRepository,
                         ReceitaRepository receitaRepository) {
        this.consultaRepository = consultaRepository;
        this.laudoRepository = laudoRepository;
        this.receitaRepository = receitaRepository;
    }

    /** Paciente dono dos dados, ou médico que já atendeu esse paciente. */
    @Transactional(readOnly = true)
    public void exigirAcessoPaciente(UsuarioLogado logado, Long pacienteId) {
        if (logado == null || pacienteId == null) negar();
        if (PACIENTE.equals(logado.tipo()) && logado.id().equals(pacienteId)) return;
        if (MEDICO.equals(logado.tipo())
                && consultaRepository.existsByMedicoUsuarioIdAndPacienteUsuarioId(logado.id(), pacienteId)) {
            return;
        }
        negar();
    }

    /** Paciente ou médico que participam da consulta. */
    @Transactional(readOnly = true)
    public void exigirParticipante(UsuarioLogado logado, Long consultaId) {
        if (logado == null) negar();
        Consulta c = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada"));
        boolean paciente = PACIENTE.equals(logado.tipo())
                && c.getPaciente().getUsuarioId().equals(logado.id());
        boolean medico = MEDICO.equals(logado.tipo())
                && c.getMedico().getUsuarioId().equals(logado.id());
        if (!paciente && !medico) negar();
    }

    /** Quem pode ver o laudo: o paciente dele ou um médico que atende esse paciente. */
    @Transactional(readOnly = true)
    public void exigirAcessoLaudo(UsuarioLogado logado, Long laudoId) {
        Laudo l = laudoRepository.findById(laudoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Laudo não encontrado"));
        exigirAcessoPaciente(logado, l.getPaciente().getUsuarioId());
    }

    /** Quem pode ver a receita: participantes da consulta que a originou. */
    @Transactional(readOnly = true)
    public void exigirAcessoReceita(UsuarioLogado logado, Long receitaId) {
        Receita r = receitaRepository.findById(receitaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Receita não encontrada"));
        exigirParticipante(logado, r.getConsulta().getConsultaId());
    }

    /** Quem pode alterar a receita: somente o médico da consulta. */
    @Transactional(readOnly = true)
    public void exigirMedicoDaReceita(UsuarioLogado logado, Long receitaId) {
        if (logado == null || !MEDICO.equals(logado.tipo())) negar();
        Receita r = receitaRepository.findById(receitaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Receita não encontrada"));
        if (!r.getConsulta().getMedico().getUsuarioId().equals(logado.id())) negar();
    }

    private static void negar() {
        throw new AccessDeniedException("Acesso negado");
    }
}
