package br.dev.matheus.vyta.service;

import br.dev.matheus.vyta.dto.EnderecoRequest;
import br.dev.matheus.vyta.dto.MedicoRequest;
import br.dev.matheus.vyta.dto.MedicoResponse;
import br.dev.matheus.vyta.dto.MedicoResumoResponse;
import br.dev.matheus.vyta.dto.MedicoUpdateRequest;
import br.dev.matheus.vyta.dto.PacienteResumoResponse;
import br.dev.matheus.vyta.exception.RecursoNaoEncontradoException;
import br.dev.matheus.vyta.exception.RegraNegocioException;
import br.dev.matheus.vyta.model.Endereco;
import br.dev.matheus.vyta.model.Medico;
import br.dev.matheus.vyta.model.Paciente;
import br.dev.matheus.vyta.model.enums.StatusConta;
import br.dev.matheus.vyta.repository.ConsultaRepository;
import br.dev.matheus.vyta.repository.EnderecoRepository;
import br.dev.matheus.vyta.repository.MedicoRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {

    private static final int IDADE_MINIMA = 18;

    private final MedicoRepository medicoRepository;
    private final ConsultaRepository consultaRepository;
    private final EnderecoRepository enderecoRepository;
    private final EnderecoService enderecoService;
    private final UsuarioRegras usuarioRegras;
    private final PasswordEncoder passwordEncoder;

    public MedicoService(MedicoRepository medicoRepository,
                         ConsultaRepository consultaRepository,
                         EnderecoRepository enderecoRepository,
                         EnderecoService enderecoService,
                         UsuarioRegras usuarioRegras,
                         PasswordEncoder passwordEncoder) {
        this.medicoRepository = medicoRepository;
        this.consultaRepository = consultaRepository;
        this.enderecoRepository = enderecoRepository;
        this.enderecoService = enderecoService;
        this.usuarioRegras = usuarioRegras;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public MedicoResponse criar(MedicoRequest req) {
        var d = usuarioRegras.validarNovo(
                req.cpf(), req.email(), req.telefone(), req.telContatoEmergencia(),
                req.dataNascimento(), IDADE_MINIMA, req.enderecoId(), req.endereco());

        String crm = req.crm().trim();
        String crmUf = req.crmUf().trim().toUpperCase();
        if (medicoRepository.existsByCrmAndCrmUf(crm, crmUf)) {
            throw new RegraNegocioException("CRM já cadastrado para esta UF");
        }

        Medico m = new Medico();
        m.setNome(req.nome().trim());
        m.setCpf(d.cpf());
        m.setEmail(d.email());
        m.setTelefone(d.telefone());
        m.setDataNascimento(req.dataNascimento());
        m.setSexoBiologico(req.sexoBiologico());
        m.setIdentidadeGenero(req.identidadeGenero().trim());
        m.setNomeContatoEmergencia(req.nomeContatoEmergencia().trim());
        m.setTelContatoEmergencia(d.telEmergencia());
        m.setStatusConta(StatusConta.ATIVA); // ajuste ao valor real do seu enum
        m.setSenha(passwordEncoder.encode(req.senha()));
        m.setEndereco(resolverEndereco(req.enderecoId(), req.endereco()));
        m.setCrm(crm);
        m.setCrmUf(crmUf);
        m.setEspecialidade(req.especialidade().trim());

        return MedicoResponse.de(medicoRepository.save(m));
    }

    @Transactional
    public MedicoResponse atualizar(Long id, MedicoUpdateRequest req) {
        Medico m = medicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Médico não encontrado"));

        var d = usuarioRegras.validarAtualizacao(
                id, req.email(), req.telefone(), req.telContatoEmergencia(),
                req.dataNascimento(), IDADE_MINIMA, req.enderecoId(), req.endereco());

        m.setNome(req.nome().trim());
        m.setEmail(d.email());
        m.setTelefone(d.telefone());
        m.setDataNascimento(req.dataNascimento());
        m.setSexoBiologico(req.sexoBiologico());
        m.setIdentidadeGenero(req.identidadeGenero().trim());
        m.setNomeContatoEmergencia(req.nomeContatoEmergencia().trim());
        m.setTelContatoEmergencia(d.telEmergencia());
        m.setEspecialidade(req.especialidade().trim());
        if (req.enderecoId() != null || req.endereco() != null) { // sem endereço no corpo = mantém o atual
            m.setEndereco(resolverEndereco(req.enderecoId(), req.endereco()));
        }

        return MedicoResponse.de(medicoRepository.save(m));
    }

    @Transactional(readOnly = true)
    public MedicoResponse buscar(Long id) {
        return medicoRepository.findById(id)
                .map(MedicoResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Médico não encontrado"));
    }

    /** Lista pública (sem CPF/e-mail/telefone) para o paciente escolher o médico. */
    @Transactional(readOnly = true)
    public List<MedicoResumoResponse> listar() {
        return medicoRepository.findAll().stream().map(MedicoResumoResponse::de).toList();
    }

    /** Pacientes que têm ou tiveram consulta com o médico, sem repetição. */
    @Transactional(readOnly = true)
    public List<PacienteResumoResponse> pacientesDoMedico(Long medicoId) {
        if (!medicoRepository.existsById(medicoId)) {
            throw new RecursoNaoEncontradoException("Médico não encontrado");
        }
        return consultaRepository.findPacientesDoMedico(medicoId).stream()
                .sorted(Comparator.comparing(Paciente::getNome, String.CASE_INSENSITIVE_ORDER))
                .map(PacienteResumoResponse::de)
                .toList();
    }

    private Endereco resolverEndereco(Long enderecoId, EnderecoRequest request) {
        if (enderecoId != null) {
            return enderecoRepository.findById(enderecoId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Endereço não encontrado"));
        }
        if (request != null) {
            return enderecoService.criarEntidade(request); // ajuste ao método real do seu EnderecoService
        }
        return null;
    }
}