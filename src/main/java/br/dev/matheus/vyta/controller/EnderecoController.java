package br.dev.matheus.vyta.controller;

import br.dev.matheus.vyta.dto.EnderecoRequest;
import br.dev.matheus.vyta.dto.EnderecoResponse;
import br.dev.matheus.vyta.service.EnderecoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
    
    private final EnderecoService service;
    
    public EnderecoController(EnderecoService service) {
        this.service = service;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) 
    public EnderecoResponse criar(@Valid @RequestBody EnderecoRequest req) {
        return service.criar(req);
    }
    
    @GetMapping("/{id}")
    public EnderecoResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
    
    @GetMapping
    public List<EnderecoResponse> listar() {
        return service.listar();
    }
    
    @PutMapping("/{id}")
    public EnderecoResponse atualizar(@PathVariable Long id,
                                      @Valid @RequestBody EnderecoRequest req) {
        return service.atualizar(id, req);
    }
}
