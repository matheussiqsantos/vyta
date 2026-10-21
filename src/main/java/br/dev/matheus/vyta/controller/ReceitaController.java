package br.dev.matheus.vyta.controller;

import br.dev.matheus.vyta.dto.ReceitaItemRequest;
import br.dev.matheus.vyta.dto.ReceitaRequest;
import br.dev.matheus.vyta.dto.ReceitaResponse;
import br.dev.matheus.vyta.service.ReceitaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReceitaController {

    private final ReceitaService service;

    public ReceitaController(ReceitaService service) {
        this.service = service;
    }

    @PostMapping("{consultaId}/receitas")
    @ResponseStatus(HttpStatus.CREATED)
    public ReceitaResponse criar(@Valid @RequestBody ReceitaRequest req) {
        return service.criar(req);
    }

    @PostMapping("/receitas/{id}/itens")
    @ResponseStatus(HttpStatus.CREATED)
    public ReceitaResponse adicionarItem(@PathVariable Long id,
            @Valid @RequestBody ReceitaItemRequest req) {
        return service.adicionarItem(id, req);
    }

    @GetMapping("/consultas/{consultaId}/receitas")
    public List<ReceitaResponse> listarPorConsulta(@PathVariable Long consultaId) {
        return service.listarPorConsulta(consultaId);
    }

    @GetMapping("/receitas/{id}")
    public ReceitaResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("pacientes/{pacienteId}/receitas")
    public List<ReceitaResponse> listarPorPaciente(@PathVariable Long pacienteId) {
        return service.listarPorPaciente(pacienteId);
    }

    @DeleteMapping("/receitas/{id}/itens/{itemId}")
    public ReceitaResponse removerItem(@PathVariable Long id, @PathVariable Long itemId) {
        return service.removerItem(id, itemId);
    }
}
