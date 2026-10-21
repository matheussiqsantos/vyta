package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.enums.StatusResultado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LaudoResultadoRequest(
        @NotBlank @Size(max = 255) String nome,
        @NotBlank @Size(max = 255) String valor,
        @Size(max = 50) String unidade,
        @Size(max = 255) String referencia,
        StatusResultado status
) {}