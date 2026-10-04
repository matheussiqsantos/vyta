package br.dev.matheus.vyta.controller;

import br.dev.matheus.vyta.dto.PacienteRequest;
import br.dev.matheus.vyta.dto.PacienteResponse;
import br.dev.matheus.vyta.service.PacienteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    
    private final PacienteService service;
    
    public PacienteController(PacienteService service) {
        this.service = service;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PacienteResponse criar(@Valid @RequestBody PacienteRequest req) {
        return service.criar(req);
    }
    
    @GetMapping("/{id}")
    public PacienteResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
    
    @GetMapping
    public List<PacienteResponse> listar() {
        return service.listar();
    }
}
