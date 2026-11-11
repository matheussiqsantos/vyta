package br.dev.matheus.vyta.controller;

import br.dev.matheus.vyta.dto.ReceitaItemRequest;
import br.dev.matheus.vyta.dto.ReceitaRequest;
import br.dev.matheus.vyta.dto.ReceitaResponse;
import br.dev.matheus.vyta.security.AcessoClinico;
import br.dev.matheus.vyta.security.Autorizacao;
import br.dev.matheus.vyta.security.UsuarioLogado;
import br.dev.matheus.vyta.service.ReceitaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReceitaController {

    private final ReceitaService service;
    private final AcessoClinico acesso;

    public ReceitaController(ReceitaService service, AcessoClinico acesso) {
        this.service = service;
        this.acesso = acesso;
    }

    @PostMapping("/receitas/{id}/itens")
    @ResponseStatus(HttpStatus.CREATED)
    public ReceitaResponse adicionarItem(@PathVariable Long id,
            @Valid @RequestBody ReceitaItemRequest req,
            @AuthenticationPrincipal UsuarioLogado logado) {
        acesso.exigirMedicoDaReceita(logado, id);
        return service.adicionarItem(id, req);
    }

    /** O service confere que o médico logado é o médico da consulta. */
    @PostMapping("/consultas/{consultaId}/receitas")
    public ResponseEntity<ReceitaResponse> criar(@PathVariable Long consultaId,
            @Valid @RequestBody ReceitaRequest req, @AuthenticationPrincipal UsuarioLogado logado) {
        Autorizacao.exigirTipo(logado, "MEDICO");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(consultaId, req, logado.id()));
    }

    @GetMapping("/consultas/{consultaId}/receitas")
    public List<ReceitaResponse> listarPorConsulta(@PathVariable Long consultaId,
            @AuthenticationPrincipal UsuarioLogado logado) {
        acesso.exigirParticipante(logado, consultaId);
        return service.listarPorConsulta(consultaId);
    }

    @GetMapping("/receitas/{id}")
    public ReceitaResponse buscar(@PathVariable Long id,
            @AuthenticationPrincipal UsuarioLogado logado) {
        acesso.exigirAcessoReceita(logado, id);
        return service.buscarPorId(id);
    }

    @GetMapping("/pacientes/{pacienteId}/receitas")
    public List<ReceitaResponse> listarPorPaciente(@PathVariable Long pacienteId,
            @AuthenticationPrincipal UsuarioLogado logado) {
        acesso.exigirAcessoPaciente(logado, pacienteId);
        return service.listarPorPaciente(pacienteId);
    }

    @DeleteMapping("/receitas/{id}/itens/{itemId}")
    public ReceitaResponse removerItem(@PathVariable Long id, @PathVariable Long itemId,
            @AuthenticationPrincipal UsuarioLogado logado) {
        acesso.exigirMedicoDaReceita(logado, id);
        return service.removerItem(id, itemId);
    }
}
