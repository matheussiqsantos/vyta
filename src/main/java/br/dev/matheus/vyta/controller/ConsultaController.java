package br.dev.matheus.vyta.controller;

import br.dev.matheus.vyta.dto.AlterarStatusRequest;
import br.dev.matheus.vyta.dto.ConsultaRequest;
import br.dev.matheus.vyta.dto.ConsultaResponse;
import br.dev.matheus.vyta.model.enums.StatusConsulta;
import br.dev.matheus.vyta.service.ConsultaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/consultas")
public class ConsultaController {
 
    private final ConsultaService service;
    
    public ConsultaController(ConsultaService service) {
        this.service = service;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultaResponse agendar(@Valid @RequestBody ConsultaRequest req) {
        return service.agendar(req);
    }
    
    @GetMapping("/{id}")
    public ConsultaResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
    
    @GetMapping("/paciente/{pacienteId}")
    public List<ConsultaResponse> listarPorPaciente(@PathVariable Long pacienteId) {
        return service.listarPorPaciente(pacienteId);
    }
    
    @GetMapping("/medico/{medicoId}")
    public List<ConsultaResponse> listarPorMedico(@PathVariable Long medicoId) {
        return service.listarPorMedico(medicoId);
    }
    
    @PatchMapping("/{id}/status")
    public ConsultaResponse alterarStatus(@PathVariable Long id,
                                          @Valid @RequestBody AlterarStatusRequest req) {
        return service.alterarStatus(id, req.status());
    }
}
