package br.dev.matheus.vyta.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReagendarConsultaRequest(
        @NotNull @FutureOrPresent LocalDate dataConsulta,
        @NotNull LocalTime horario,
        @Size(max = 1000, message = "O motivo deve ter no máximo 1000 caracteres") String motivo
) {}