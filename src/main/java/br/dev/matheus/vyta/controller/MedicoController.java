package br.dev.matheus.vyta.controller;

import br.dev.matheus.vyta.dto.MedicoRequest;
import br.dev.matheus.vyta.dto.MedicoResponse;
import br.dev.matheus.vyta.dto.MedicoResumoResponse;
import br.dev.matheus.vyta.dto.MedicoUpdateRequest;
import br.dev.matheus.vyta.dto.PacienteResumoResponse;
import br.dev.matheus.vyta.security.Autorizacao;
import br.dev.matheus.vyta.security.UsuarioLogado;
import br.dev.matheus.vyta.service.MedicoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @PostMapping // público: cadastro
    public ResponseEntity<MedicoResponse> criar(@Valid @RequestBody MedicoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(req));
    }

    @GetMapping // qualquer usuário logado; só dados públicos
    public List<MedicoResumoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public MedicoResponse buscar(@PathVariable Long id, @AuthenticationPrincipal UsuarioLogado logado) {
        Autorizacao.exigirMesmoUsuario(id, logado);
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public MedicoResponse atualizar(@PathVariable Long id,
                                    @Valid @RequestBody MedicoUpdateRequest req,
                                    @AuthenticationPrincipal UsuarioLogado logado) {
        Autorizacao.exigirMesmoUsuario(id, logado);
        return service.atualizar(id, req);
    }

    @GetMapping("/{id}/pacientes")
    public List<PacienteResumoResponse> pacientes(@PathVariable Long id,
                                                  @AuthenticationPrincipal UsuarioLogado logado) {
        Autorizacao.exigirMesmoUsuario(id, logado);
        return service.pacientesDoMedico(id);
    }
}