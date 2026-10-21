package br.dev.matheus.vyta.service;

import br.dev.matheus.vyta.dto.AlterarStatusRequest;
import br.dev.matheus.vyta.dto.ConsultaRequest;
import br.dev.matheus.vyta.dto.ConsultaResponse;
import br.dev.matheus.vyta.exception.RecursoNaoEncontradoException;
import br.dev.matheus.vyta.exception.RegraNegocioException;
import br.dev.matheus.vyta.model.Consulta;
import br.dev.matheus.vyta.model.Medico;
import br.dev.matheus.vyta.model.Paciente;
import br.dev.matheus.vyta.model.enums.ModalidadeConsulta;
import br.dev.matheus.vyta.model.enums.StatusConsulta;
import br.dev.matheus.vyta.model.enums.StatusConta;
import br.dev.matheus.vyta.repository.ConsultaRepository;
import br.dev.matheus.vyta.repository.MedicoRepository;
import br.dev.matheus.vyta.repository.PacienteRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {

    private static final ZoneId ZONA = ZoneId.of("America/Sao_Paulo");

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final EnderecoService enderecoService;

    public ConsultaService(ConsultaRepository consultaRepository,
                           PacienteRepository pacienteRepository,
                           MedicoRepository medicoRepository,
                           EnderecoService enderecoService) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.enderecoService = enderecoService;
    }

    @Transactional
    public ConsultaResponse agendar(ConsultaRequest req) {
        Paciente p = pacienteRepository.findById(req.pacienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));
        Medico m = medicoRepository.findById(req.medicoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Médico não encontrado"));

        if (p.getStatusConta() != StatusConta.ATIVA) {
            throw new RegraNegocioException("Paciente com conta inativa não pode agendar consultas");
        }
        if (m.getStatusConta() != StatusConta.ATIVA) {
            throw new RegraNegocioException("Médico com conta inativa não recebe consultas");
        }

        LocalDateTime quando = LocalDateTime.of(req.dataConsulta(), req.horario());
        if (!quando.isAfter(LocalDateTime.now(ZONA))) {
            throw new RegraNegocioException("A consulta deve ser agendada para uma data e hora futuras");
        }

        if (consultaRepository.existsByMedicoUsuarioIdAndDataConsultaAndHorarioAndStatusConsultaNot(
                m.getUsuarioId(), req.dataConsulta(), req.horario(), StatusConsulta.CANCELADA)) {
            throw new RegraNegocioException("O médico já possui consulta neste horário");
        }
        if (consultaRepository.existsByPacienteUsuarioIdAndDataConsultaAndHorarioAndStatusConsultaNot(
                p.getUsuarioId(), req.dataConsulta(), req.horario(), StatusConsulta.CANCELADA)) {
            throw new RegraNegocioException("O paciente já possui consulta neste horário");
        }

        boolean temEndereco = req.enderecoId() != null || req.endereco() != null;
        if (req.modalidade() == ModalidadeConsulta.PRESENCIAL && !temEndereco) {
            throw new RegraNegocioException("Consulta presencial exige o endereço do atendimento");
        }
        if (req.modalidade() == ModalidadeConsulta.ONLINE && temEndereco) {
            throw new RegraNegocioException("Consulta online não deve ter endereço");
        }

        Consulta c = new Consulta();
        c.setPaciente(p);
        c.setMedico(m);
        c.setDataConsulta(req.dataConsulta());
        c.setHorario(req.horario());
        c.setTipoConsulta(req.tipoConsulta().trim());
        c.setModalidade(req.modalidade());
        c.setConvenio(req.convenio() == null || req.convenio().isBlank() ? null : req.convenio().trim());
        c.setStatusConsulta(StatusConsulta.AGENDADA);
        c.setEndereco(enderecoService.resolver(req.enderecoId(), req.endereco()));

        return ConsultaResponse.de(consultaRepository.save(c));
    }

    @Transactional
    public ConsultaResponse alterarStatus(Long id, AlterarStatusRequest req) {
        Consulta c = buscarEntidade(id);
        StatusConsulta novo = req.status();

        if (!c.getStatusConsulta().podeMudarPara(novo)) {
            throw new RegraNegocioException(
                    "Não é possível mudar a consulta de " + c.getStatusConsulta() + " para " + novo);
        }

        boolean jaOcorreu = !LocalDateTime.of(c.getDataConsulta(), c.getHorario())
                .isAfter(LocalDateTime.now(ZONA));

        if (novo == StatusConsulta.CONFIRMADA && jaOcorreu) {
            throw new RegraNegocioException("Não é possível confirmar uma consulta que já passou");
        }
        if (novo == StatusConsulta.REALIZADA && !jaOcorreu) {
            throw new RegraNegocioException("A consulta ainda não ocorreu, não pode ser marcada como realizada");
        }
        if (novo == StatusConsulta.CANCELADA && jaOcorreu) {
            throw new RegraNegocioException("Não é possível cancelar uma consulta que já passou");
        }

        c.setStatusConsulta(novo);
        return ConsultaResponse.de(c);
    }

    @Transactional(readOnly = true)
    public ConsultaResponse buscarPorId(Long id) {
        return ConsultaResponse.de(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponse> listarPorPaciente(Long pacienteId) {
        return consultaRepository.findByPacienteUsuarioIdOrderByDataConsultaAscHorarioAsc(pacienteId)
                .stream().map(ConsultaResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponse> listarPorMedico(Long medicoId) {
        return consultaRepository.findByMedicoUsuarioIdOrderByDataConsultaAscHorarioAsc(medicoId)
                .stream().map(ConsultaResponse::de).toList();
    }

    private Consulta buscarEntidade(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada"));
    }
}