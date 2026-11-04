package br.dev.matheus.vyta.controller;

import br.dev.matheus.vyta.dto.PacienteRequest;
import br.dev.matheus.vyta.dto.PacienteResponse;
import br.dev.matheus.vyta.dto.PacienteUpdateRequest;
import br.dev.matheus.vyta.security.Autorizacao;
import br.dev.matheus.vyta.security.UsuarioLogado;
import br.dev.matheus.vyta.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @PostMapping // público: cadastro
    public ResponseEntity<PacienteResponse> criar(@Valid @RequestBody PacienteRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(req));
    }

    @GetMapping("/{id}")
    public PacienteResponse buscar(@PathVariable Long id, @AuthenticationPrincipal UsuarioLogado logado) {
        Autorizacao.exigirMesmoUsuario(id, logado);
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public PacienteResponse atualizar(@PathVariable Long id,
                                      @Valid @RequestBody PacienteUpdateRequest req,
                                      @AuthenticationPrincipal UsuarioLogado logado) {
        Autorizacao.exigirMesmoUsuario(id, logado);
        return service.atualizar(id, req);
    }
}