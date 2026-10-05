package br.dev.matheus.vyta.service;

import br.dev.matheus.vyta.dto.EnderecoRequest;
import br.dev.matheus.vyta.dto.EnderecoResponse;
import br.dev.matheus.vyta.exception.RecursoNaoEncontradoException;
import br.dev.matheus.vyta.exception.RegraNegocioException;
import br.dev.matheus.vyta.model.Endereco;
import br.dev.matheus.vyta.repository.EnderecoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnderecoService {
    
    private final EnderecoRepository enderecoRepository;
    
    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }
    
    public Endereco resolver(Long enderecoId, EnderecoRequest novo) {
        if (enderecoId != null && novo != null) {
            throw new RegraNegocioException(
                    "Informe enderecoId ou endereço, não os dois");
        }
        if (enderecoId != null) {
            return buscarEntidade(enderecoId);
        }
        if (novo != null) {
            Endereco e = new Endereco();
            preencher(e, novo);
            return enderecoRepository.save(e);
        }
        return null;
    }
    
    @Transactional
    public EnderecoResponse criar(EnderecoRequest req) {
        Endereco e = new Endereco();
        preencher(e, req);
        return EnderecoResponse.de(enderecoRepository.save(e));
    }
    
    @Transactional(readOnly = true)
    public EnderecoResponse buscarPorId(Long id) {
        return EnderecoResponse.de(buscarEntidade(id));
    }
    
    @Transactional(readOnly = true)
    public List<EnderecoResponse> listar() {
        return enderecoRepository.findAll().stream()
                .map(EnderecoResponse::de)
                .toList();
    }
    
    @Transactional
    public EnderecoResponse atualizar(Long id, EnderecoRequest req) {
        Endereco e = buscarEntidade(id);
        preencher(e, req);
        return EnderecoResponse.de(enderecoRepository.save(e));
    }
    
    private Endereco buscarEntidade(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Endereço não encontrado"));
    }
    
    private void preencher(Endereco e, EnderecoRequest req) {
        e.setCep(req.cep());
        e.setLogradouro(req.logradouro().trim());
        e.setNumero(req.numero());
        e.setComplemento(req.complemento());
        e.setBairro(req.bairro().trim());
        e.setCidade(req.cidade().trim());
        e.setEstado(req.estado().trim().toUpperCase());
        e.setPais(req.pais() == null || req.pais().isBlank() ? "Brasil" : req.pais().trim());
    }
}
