package br.dev.matheus.vyta.controller;

import br.dev.matheus.vyta.dto.*;
import br.dev.matheus.vyta.security.AcessoClinico;
import br.dev.matheus.vyta.security.Autorizacao;
import br.dev.matheus.vyta.security.UsuarioLogado;
import br.dev.matheus.vyta.service.LaudoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/laudos")
public class LaudoController {

    private final LaudoService service;
    private final AcessoClinico acesso;

    public LaudoController(LaudoService service, AcessoClinico acesso) {
        this.service = service;
        this.acesso = acesso;
    }

    /** Somente médico cria laudo, para paciente que atende, e como solicitante ele mesmo. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LaudoResponse criar(@Valid @RequestBody LaudoRequest req,
                               @AuthenticationPrincipal UsuarioLogado logado) {
        Autorizacao.exigirTipo(logado, "MEDICO");
        acesso.exigirAcessoPaciente(logado, req.pacienteId());
        if (req.medicoSolicitanteId() != null && !req.medicoSolicitanteId().equals(logado.id())) {
            throw new AccessDeniedException("Acesso negado");
        }
        return service.criar(req);
    }

    @GetMapping("/{id}")
    public LaudoResponse buscar(@PathVariable Long id,
                                @AuthenticationPrincipal UsuarioLogado logado) {
        acesso.exigirAcessoLaudo(logado, id);
        return service.buscarPorId(id);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<LaudoResponse> listarPorPaciente(@PathVariable Long pacienteId,
                                                 @AuthenticationPrincipal UsuarioLogado logado) {
        acesso.exigirAcessoPaciente(logado, pacienteId);
        return service.listarPorPaciente(pacienteId);
    }

    @GetMapping("/consulta/{consultaId}")
    public List<LaudoResponse> listarPorConsulta(@PathVariable Long consultaId,
                                                 @AuthenticationPrincipal UsuarioLogado logado) {
        acesso.exigirParticipante(logado, consultaId);
        return service.listarPorConsulta(consultaId);
    }

    @PostMapping("/{id}/resultados")
    @ResponseStatus(HttpStatus.CREATED)
    public LaudoResponse adicionarResultado(@PathVariable Long id,
                                            @Valid @RequestBody LaudoResultadoRequest req,
                                            @AuthenticationPrincipal UsuarioLogado logado) {
        Autorizacao.exigirTipo(logado, "MEDICO");
        acesso.exigirAcessoLaudo(logado, id);
        return service.adicionarResultado(id, req);
    }

    @DeleteMapping("/{id}/resultados/{resultadoId}")
    public LaudoResponse removerResultado(@PathVariable Long id, @PathVariable Long resultadoId,
                                          @AuthenticationPrincipal UsuarioLogado logado) {
        Autorizacao.exigirTipo(logado, "MEDICO");
        acesso.exigirAcessoLaudo(logado, id);
        return service.removerResultado(id, resultadoId);
    }
}
