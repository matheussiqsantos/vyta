package br.dev.matheus.vyta.service;

import br.dev.matheus.vyta.dto.MedicoRequest;
import br.dev.matheus.vyta.dto.MedicoResponse;
import br.dev.matheus.vyta.exception.RecursoNaoEncontradoException;
import br.dev.matheus.vyta.exception.RegraNegocioException;
import br.dev.matheus.vyta.model.Medico;
import br.dev.matheus.vyta.model.enums.StatusConta;
import br.dev.matheus.vyta.repository.MedicoRepository;
import br.dev.matheus.vyta.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {
    
    private final MedicoRepository medicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoService enderecoService;
    private final PasswordEncoder passwordEncoder;
    
    public MedicoService(MedicoRepository medicoRepository,
                         UsuarioRepository usuarioRepository,
                         EnderecoService enderecoService,
                         PasswordEncoder passwordEncoder) {
        this.medicoRepository = medicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.enderecoService = enderecoService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Transactional
    public MedicoResponse criar(MedicoRequest req) {
        String email = req.email().trim().toLowerCase();
        String crmUf = req.crmUf().trim().toUpperCase();
        
        if (usuarioRepository.existsByEmail(email)) {
            throw new RegraNegocioException("E-mail já cadastrado");
        }
        if (usuarioRepository.existsByCpf(req.cpf())) {
            throw new RegraNegocioException("CPF já cadastrado");
        }
        if (medicoRepository.existsByCrmAndCrmUf(req.crm(), crmUf)) {
            throw new RegraNegocioException("CRM já cadastrado para esta UF");
        }
        
        Medico m = new Medico();
        m.setNome(req.nome().trim());
        m.setCpf(req.cpf());
        m.setEmail(email);
        m.setTelefone(req.telefone());
        m.setDataNascimento(req.dataNascimento());
        m.setSexoBiologico(req.sexoBiologico());
        m.setIdentidadeGenero(req.identidadeGenero());
        m.setNomeContatoEmergencia(req.nomeContatoEmergencia());
        m.setTelContatoEmergencia(req.telContatoEmergencia());
        m.setSenha(passwordEncoder.encode(req.senha()));
        m.setStatusConta(StatusConta.ATIVA);
        m.setCrm(req.crm());
        m.setCrmUf(crmUf);
        m.setEspecialidade(req.especialidade());
        m.setEndereco(enderecoService.resolver(req.enderecoId(), req.endereco()));
        
        return MedicoResponse.de(medicoRepository.save(m));
    }
    
    @Transactional(readOnly = true)
    public MedicoResponse buscarPorId(Long id) {
        return medicoRepository.findById(id)
                .map(MedicoResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Médico não encontrado"));
    }
    
    @Transactional(readOnly = true)
    public List<MedicoResponse> listar() {
        return medicoRepository.findAll().stream()
                .map(MedicoResponse::de)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public List<MedicoResponse> listarPorEspecialidade(String especialidade) {
        return medicoRepository.findByEspecialidadeContainingIgnoreCase(especialidade).stream()
                .map(MedicoResponse::de)
                .toList();
    }
}
