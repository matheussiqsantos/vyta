package br.dev.matheus.vyta.service;

import br.dev.matheus.vyta.dto.EnderecoRequest;
import br.dev.matheus.vyta.dto.PacienteRequest;
import br.dev.matheus.vyta.dto.PacienteResponse;
import br.dev.matheus.vyta.dto.PacienteUpdateRequest;
import br.dev.matheus.vyta.exception.RecursoNaoEncontradoException;
import br.dev.matheus.vyta.model.Endereco;
import br.dev.matheus.vyta.model.Paciente;
import br.dev.matheus.vyta.model.enums.StatusConta;
import br.dev.matheus.vyta.repository.EnderecoRepository;
import br.dev.matheus.vyta.repository.PacienteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

    private static final int IDADE_MINIMA = 0;

    private final PacienteRepository pacienteRepository;
    private final EnderecoRepository enderecoRepository;
    private final EnderecoService enderecoService;
    private final UsuarioRegras usuarioRegras;
    private final PasswordEncoder passwordEncoder;

    public PacienteService(PacienteRepository pacienteRepository,
                           EnderecoRepository enderecoRepository,
                           EnderecoService enderecoService,
                           UsuarioRegras usuarioRegras,
                           PasswordEncoder passwordEncoder) {
        this.pacienteRepository = pacienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.enderecoService = enderecoService;
        this.usuarioRegras = usuarioRegras;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public PacienteResponse criar(PacienteRequest req) {
        var d = usuarioRegras.validarNovo(
                req.cpf(), req.email(), req.telefone(), req.telContatoEmergencia(),
                req.dataNascimento(), IDADE_MINIMA, req.enderecoId(), req.endereco());

        Paciente p = new Paciente();
        p.setNome(req.nome().trim());
        p.setCpf(d.cpf());
        p.setEmail(d.email());
        p.setTelefone(d.telefone());
        p.setDataNascimento(req.dataNascimento());
        p.setSexoBiologico(req.sexoBiologico());
        p.setIdentidadeGenero(req.identidadeGenero().trim());
        p.setNomeContatoEmergencia(req.nomeContatoEmergencia().trim());
        p.setTelContatoEmergencia(d.telEmergencia());
        p.setStatusConta(StatusConta.ATIVA); // ajuste ao valor real do seu enum
        p.setSenha(passwordEncoder.encode(req.senha()));
        p.setEndereco(resolverEndereco(req.enderecoId(), req.endereco()));
        p.setTipoSanguineo(req.tipoSanguineo());

        return PacienteResponse.de(pacienteRepository.save(p));
    }

    @Transactional
    public PacienteResponse atualizar(Long id, PacienteUpdateRequest req) {
        Paciente p = pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));

        var d = usuarioRegras.validarAtualizacao(
                id, req.email(), req.telefone(), req.telContatoEmergencia(),
                req.dataNascimento(), IDADE_MINIMA, req.enderecoId(), req.endereco());

        p.setNome(req.nome().trim());
        p.setEmail(d.email());
        p.setTelefone(d.telefone());
        p.setDataNascimento(req.dataNascimento());
        p.setSexoBiologico(req.sexoBiologico());
        p.setIdentidadeGenero(req.identidadeGenero().trim());
        p.setNomeContatoEmergencia(req.nomeContatoEmergencia().trim());
        p.setTelContatoEmergencia(d.telEmergencia());
        p.setTipoSanguineo(req.tipoSanguineo());
        if (req.enderecoId() != null || req.endereco() != null) { // sem endereço no corpo = mantém o atual
            p.setEndereco(resolverEndereco(req.enderecoId(), req.endereco()));
        }

        return PacienteResponse.de(pacienteRepository.save(p));
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscar(Long id) {
        return pacienteRepository.findById(id)
                .map(PacienteResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));
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