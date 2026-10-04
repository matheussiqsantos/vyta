package br.dev.matheus.vyta.controller;

import br.dev.matheus.vyta.dto.MedicoRequest;
import br.dev.matheus.vyta.dto.MedicoResponse;
import br.dev.matheus.vyta.service.MedicoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/medicos")
public class MedicoController {
    
    private final MedicoService service;
    
    public MedicoController(MedicoService service) {
        this.service = service;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicoResponse criar(@Valid @RequestBody MedicoRequest req) {
        return service.criar(req);
    }
    
    @GetMapping("/{id}")
    public MedicoResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
    
    @GetMapping
    public List<MedicoResponse> listar(@RequestParam(required = false) String especialidade) {
        return especialidade == null
                ? service.listar()
                : service.listarPorEspecialidade(especialidade);
    }
}
