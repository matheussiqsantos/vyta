package br.dev.matheus.vyta.controller;

import br.dev.matheus.vyta.dto.AlterarStatusRequest;
import br.dev.matheus.vyta.dto.ConsultaRequest;
import br.dev.matheus.vyta.dto.ConsultaResponse;
import br.dev.matheus.vyta.model.enums.StatusConsulta;
import br.dev.matheus.vyta.security.AcessoClinico;
import br.dev.matheus.vyta.security.Autorizacao;
import br.dev.matheus.vyta.security.UsuarioLogado;
import br.dev.matheus.vyta.service.ConsultaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService service;
    private final AcessoClinico acesso;

    public ConsultaController(ConsultaService service, AcessoClinico acesso) {
        this.service = service;
        this.acesso = acesso;
    }

    /** Somente o próprio paciente agenda a sua consulta. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultaResponse agendar(@Valid @RequestBody ConsultaRequest req,
                                    @AuthenticationPrincipal UsuarioLogado logado) {
        Autorizacao.exigirTipo(logado, "PACIENTE");
        Autorizacao.exigirMesmoUsuario(req.pacienteId(), logado);
        return service.agendar(req);
    }

    @GetMapping("/{id}")
    public ConsultaResponse buscar(@PathVariable Long id,
                                   @AuthenticationPrincipal UsuarioLogado logado) {
        acesso.exigirParticipante(logado, id);
        return service.buscarPorId(id);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<ConsultaResponse> listarPorPaciente(@PathVariable Long pacienteId,
                                                    @AuthenticationPrincipal UsuarioLogado logado) {
        acesso.exigirAcessoPaciente(logado, pacienteId);
        return service.listarPorPaciente(pacienteId);
    }

    @GetMapping("/medico/{medicoId}")
    public List<ConsultaResponse> listarPorMedico(@PathVariable Long medicoId,
                                                  @AuthenticationPrincipal UsuarioLogado logado) {
        Autorizacao.exigirTipo(logado, "MEDICO");
        Autorizacao.exigirMesmoUsuario(medicoId, logado);
        return service.listarPorMedico(medicoId);
    }

    /** Paciente só pode cancelar; o médico pode confirmar, realizar ou cancelar. */
    @PatchMapping("/{id}/status")
    public ConsultaResponse alterarStatus(@PathVariable Long id,
            @Valid @RequestBody AlterarStatusRequest req,
            @AuthenticationPrincipal UsuarioLogado logado) {
        acesso.exigirParticipante(logado, id);
        if ("PACIENTE".equals(logado.tipo()) && req.status() != StatusConsulta.CANCELADA) {
            throw new AccessDeniedException("Acesso negado");
        }
        return service.alterarStatus(id, req);
    }
}
