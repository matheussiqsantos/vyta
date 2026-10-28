package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.enums.ModalidadeConsulta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultaRequest(
    @NotNull Long pacienteId,
    @NotNull Long medicoId,
    @NotNull LocalDate dataConsulta,
    @NotNull LocalTime horario,
    @NotBlank @Size(max = 100) String tipoConsulta,
    @NotNull ModalidadeConsulta modalidade,
    @Size(max = 100) String convenio,
    @Size(max = 1000, message = "O motivo deve ter no máximo 1000 caracteres") String motivo,
    Long enderecoId,
    @Valid EnderecoRequest endereco
) {}
