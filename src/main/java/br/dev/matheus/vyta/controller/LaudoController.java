package br.dev.matheus.vyta.controller;

import br.dev.matheus.vyta.dto.*;
import br.dev.matheus.vyta.service.LaudoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/laudos")
public class LaudoController {

    private final LaudoService service;

    public LaudoController(LaudoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LaudoResponse criar(@Valid @RequestBody LaudoRequest req) {
        return service.criar(req);
    }

    @GetMapping("/{id}")
    public LaudoResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<LaudoResponse> listarPorPaciente(@PathVariable Long pacienteId) {
        return service.listarPorPaciente(pacienteId);
    }

    @GetMapping("/consulta/{consultaId}")
    public List<LaudoResponse> listarPorConsulta(@PathVariable Long consultaId) {
        return service.listarPorConsulta(consultaId);
    }

    @PostMapping("/{id}/resultados")
    @ResponseStatus(HttpStatus.CREATED)
    public LaudoResponse adicionarResultado(@PathVariable Long id,
                                            @Valid @RequestBody LaudoResultadoRequest req) {
        return service.adicionarResultado(id, req);
    }

    @DeleteMapping("/{id}/resultados/{resultadoId}")
    public LaudoResponse removerResultado(@PathVariable Long id, @PathVariable Long resultadoId) {
        return service.removerResultado(id, resultadoId);
    }
}