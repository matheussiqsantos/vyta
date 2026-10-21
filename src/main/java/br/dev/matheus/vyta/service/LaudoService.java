package br.dev.matheus.vyta.service;

import br.dev.matheus.vyta.dto.LaudoRequest;
import br.dev.matheus.vyta.dto.LaudoResponse;
import br.dev.matheus.vyta.dto.LaudoResultadoRequest;
import br.dev.matheus.vyta.exception.RecursoNaoEncontradoException;
import br.dev.matheus.vyta.exception.RegraNegocioException;
import br.dev.matheus.vyta.model.*;
import br.dev.matheus.vyta.model.enums.StatusConta;
import br.dev.matheus.vyta.model.enums.StatusResultado;
import br.dev.matheus.vyta.repository.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LaudoService {

    private static final ZoneId ZONA = ZoneId.of("America/Sao_Paulo");

    private final LaudoRepository laudoRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultaRepository consultaRepository;

    public LaudoService(LaudoRepository laudoRepository,
                        PacienteRepository pacienteRepository,
                        MedicoRepository medicoRepository,
                        ConsultaRepository consultaRepository) {
        this.laudoRepository = laudoRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.consultaRepository = consultaRepository;
    }

    @Transactional
    public LaudoResponse criar(LaudoRequest req) {
        Paciente paciente = pacienteRepository.findById(req.pacienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));
        if (paciente.getStatusConta() != StatusConta.ATIVA) {
            throw new RegraNegocioException("Paciente com conta inativa não pode receber laudos");
        }

        if (req.dataExame().isAfter(LocalDate.now(ZONA))) {
            throw new RegraNegocioException("A data do exame não pode estar no futuro");
        }

        Medico medico = null;
        if (req.medicoSolicitanteId() != null) {
            medico = medicoRepository.findById(req.medicoSolicitanteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Médico solicitante não encontrado"));
            if (medico.getStatusConta() != StatusConta.ATIVA) {
                throw new RegraNegocioException("Médico solicitante com conta inativa");
            }
        }

        Consulta consulta = null;
        if (req.consultaId() != null) {
            consulta = consultaRepository.findById(req.consultaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada"));
            if (!consulta.getPaciente().getUsuarioId().equals(paciente.getUsuarioId())) {
                throw new RegraNegocioException("A consulta informada não pertence a este paciente");
            }
            if (medico != null
                    && !consulta.getMedico().getUsuarioId().equals(medico.getUsuarioId())) {
                throw new RegraNegocioException(
                        "O médico solicitante deve ser o médico da consulta informada");
            }
        }

        boolean semResultados = req.resultados() == null || req.resultados().isEmpty();
        boolean semTexto = vazio(req.achados()) && vazio(req.conclusao());
        if (semResultados && semTexto) {
            throw new RegraNegocioException(
                    "Informe ao menos um resultado, os achados ou a conclusão do laudo");
        }

        String url = limpar(req.arquivoUrl());
        if (url != null && !url.matches("(?i)^https?://\\S+$")) {
            throw new RegraNegocioException("URL do arquivo inválida. Use http:// ou https://");
        }

        Laudo l = new Laudo();
        l.setPaciente(paciente);
        l.setMedicoSolicitante(medico);
        l.setConsulta(consulta);
        l.setTitulo(req.titulo().trim());
        l.setTipo(req.tipo());
        l.setLaboratorio(req.laboratorio().trim());
        l.setDataExame(req.dataExame());
        l.setAchados(limpar(req.achados()));
        l.setConclusao(limpar(req.conclusao()));
        l.setArquivoUrl(url);
        if (!semResultados) {
            req.resultados().forEach(r -> l.adicionarResultado(montarResultado(r)));
        }

        return LaudoResponse.de(laudoRepository.save(l));
    }

    @Transactional
    public LaudoResponse adicionarResultado(Long laudoId, LaudoResultadoRequest req) {
        Laudo l = buscarEntidade(laudoId);
        l.adicionarResultado(montarResultado(req));
        laudoRepository.saveAndFlush(l);
        return LaudoResponse.de(l);
    }

    @Transactional
    public LaudoResponse removerResultado(Long laudoId, Long resultadoId) {
        Laudo l = buscarEntidade(laudoId);
        boolean removido = l.getResultados().removeIf(r -> r.getId().equals(resultadoId));
        if (!removido) {
            throw new RecursoNaoEncontradoException("Resultado não encontrado neste laudo");
        }
        if (l.getResultados().isEmpty() && vazio(l.getAchados()) && vazio(l.getConclusao())) {
            throw new RegraNegocioException(
                    "Não é possível remover o último resultado de um laudo sem achados nem conclusão");
        }
        laudoRepository.saveAndFlush(l);
        return LaudoResponse.de(l);
    }

    @Transactional(readOnly = true)
    public LaudoResponse buscarPorId(Long id) {
        return LaudoResponse.de(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<LaudoResponse> listarPorPaciente(Long pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new RecursoNaoEncontradoException("Paciente não encontrado");
        }
        return laudoRepository.findByPacienteUsuarioIdOrderByDataExameDesc(pacienteId)
                .stream().map(LaudoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public List<LaudoResponse> listarPorConsulta(Long consultaId) {
        return laudoRepository.findByConsultaConsultaId(consultaId)
                .stream().map(LaudoResponse::de).toList();
    }

    private Laudo buscarEntidade(Long id) {
        return laudoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Laudo não encontrado"));
    }

    private LaudoResultado montarResultado(LaudoResultadoRequest req) {
        LaudoResultado r = new LaudoResultado();
        r.setNome(req.nome().trim());
        r.setValor(req.valor().trim());
        r.setUnidade(limpar(req.unidade()));
        r.setReferencia(limpar(req.referencia()));
        r.setStatus(req.status() == null ? StatusResultado.NORMAL : req.status());
        return r;
    }

    private static boolean vazio(String s) {
        return s == null || s.isBlank();
    }

    private static String limpar(String s) {
        return vazio(s) ? null : s.trim();
    }
}