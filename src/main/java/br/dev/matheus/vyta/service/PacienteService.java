package br.dev.matheus.vyta.service;

import br.dev.matheus.vyta.dto.PacienteRequest;
import br.dev.matheus.vyta.dto.PacienteResponse;
import br.dev.matheus.vyta.exception.RecursoNaoEncontradoException;
import br.dev.matheus.vyta.exception.RegraNegocioException;
import br.dev.matheus.vyta.model.Paciente;
import br.dev.matheus.vyta.model.enums.StatusConta;
import br.dev.matheus.vyta.repository.PacienteRepository;
import br.dev.matheus.vyta.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {
    
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoService enderecoService;
    private final PasswordEncoder passwordEncoder;
    
    public PacienteService(PacienteRepository pacienteRepository,
                           UsuarioRepository usuarioRepository,
                           EnderecoService enderecoService,
                           PasswordEncoder passwordEncoder) {
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.enderecoService = enderecoService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Transactional
    public PacienteResponse criar(PacienteRequest req) {
        String email = req.email().trim().toLowerCase();
        
        if (usuarioRepository.existsByEmail(email)) {
            throw new RegraNegocioException("E-mail já cadastrado");
        }
        if (usuarioRepository.existsByCpf(req.cpf())) {
            throw new RegraNegocioException("CPF já cadastrado");
        }
        
        Paciente p = new Paciente();
        p.setNome(req.nome().trim());
        p.setCpf(req.cpf());
        p.setEmail(email);
        p.setTelefone(req.telefone());
        p.setDataNascimento(req.dataNascimento());
        p.setSexoBiologico(req.sexoBiologico());
        p.setIdentidadeGenero(req.identidadeGenero());
        p.setNomeContatoEmergencia(req.nomeContatoEmergencia());
        p.setTelContatoEmergencia(req.telContatoEmergencia());
        p.setSenha(passwordEncoder.encode(req.senha()));
        p.setStatusConta(StatusConta.ATIVA);
        p.setTipoSanguineo(req.tipoSanguineo());
        
        p.setEndereco(enderecoService.resolver(req.enderecoId(), req.endereco()));
        
        return PacienteResponse.de(pacienteRepository.save(p));
        
        
    }
    
    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        return PacienteResponse.de(buscarEntidade(id));
    }
    
    @Transactional(readOnly = true)
    public List<PacienteResponse> listar() {
        return pacienteRepository.findAll().stream()
                .map(PacienteResponse::de)
                .toList();
    }
    
    private Paciente buscarEntidade(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));
    }
}
