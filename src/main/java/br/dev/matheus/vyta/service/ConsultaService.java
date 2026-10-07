package br.dev.matheus.vyta.service;

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
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {
    
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
        Paciente paciente = pacienteRepository.findById(req.pacienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado."));
        Medico medico = medicoRepository.findById(req.medicoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Médico não encontrado."));
        
        if (paciente.getStatusConta() != StatusConta.ATIVA
                || medico.getStatusConta() != StatusConta.ATIVA) {
            throw new RegraNegocioException("Paciente e médico precisam ter a conta ativa.");
        }
        
        if (LocalDateTime.of(req.dataConsulta(), req.horario()).isBefore(LocalDateTime.now())) {
            throw new RegraNegocioException("Não é possível agendar no passado.");
        }
        
        if (consultaRepository
                .existsByMedicoUsuarioIdAndDataConsultaAndHorarioAndStatusConsultaNot(
                        medico.getUsuarioId(), req.dataConsulta(), req.horario(),
                        StatusConsulta.CANCELADA)) {
            throw new RegraNegocioException("O médico já possui consulta nesse horário.");
        }
        
        if (consultaRepository
                .existsByPacienteUsuarioIdAndDataConsultaAndHorarioAndStatusConsultaNot(
                        paciente.getUsuarioId(), req.dataConsulta(), req.horario(),
                        StatusConsulta.CANCELADA)) {
            throw new RegraNegocioException("O paciente já possui consulta nesse horário.");
        }
        
        boolean temEndereco = req.enderecoId() != null || req.endereco() != null;
        if (req.modalidade() == ModalidadeConsulta.PRESENCIAL && !temEndereco) {
            throw new RegraNegocioException("Consulta presencial exige o local (endereço).");
        }
        if (req.modalidade() == ModalidadeConsulta.ONLINE && temEndereco) {
            throw new RegraNegocioException("Consulta online não deve ter endereço nenhum.");
        }
        
        Consulta c = new Consulta();
        c.setPaciente(paciente);
        c.setMedico(medico);
        c.setDataConsulta(req.dataConsulta());
        c.setHorario(req.horario());
        c.setTipoConsulta(req.tipoConsulta().trim());
        c.setModalidade(req.modalidade());
        c.setStatusConsulta(StatusConsulta.AGENDADA);
        c.setConvenio(req.convenio());
        c.setEndereco(enderecoService.resolver(req.enderecoId(), req.endereco()));
                
        return ConsultaResponse.de(consultaRepository.save(c));
    }   
    
    @Transactional(readOnly = true)
    public ConsultaResponse buscarPorId(Long id) {
        return ConsultaResponse.de(buscarEntidade(id));
    }
    
    @Transactional(readOnly = true)
    public List<ConsultaResponse> listarPorPaciente(Long pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new RecursoNaoEncontradoException("Paciente não encontrado.");
        }
        return consultaRepository.findByPacienteUsuarioIdOrderByDataConsultaAscHorarioAsc(pacienteId)
                .stream().map(ConsultaResponse::de).toList();
    }
    
    @Transactional(readOnly = true)
    public List<ConsultaResponse> listarPorMedico(Long medicoId) {
        if (!medicoRepository.existsById(medicoId)) {
            throw new RecursoNaoEncontradoException("Médico não encontrado");
        }
        return consultaRepository.findByMedicoUsuarioIdOrderByDataConsultaAscHorarioAsc(medicoId)
                .stream().map(ConsultaResponse::de).toList();
    }

    @Transactional
    public ConsultaResponse alterarStatus(Long id, StatusConsulta novo) {
        Consulta c = buscarEntidade(id);
        if (!c.getStatusConsulta().podeMudarPara(novo)) {
            throw new RegraNegocioException(
                    "Não é possível mudar de " + c.getStatusConsulta() + " para " + novo);
        }
        c.setStatusConsulta(novo);
        return ConsultaResponse.de(consultaRepository.save(c));
    }

    private Consulta buscarEntidade(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada"));
    } 
}
