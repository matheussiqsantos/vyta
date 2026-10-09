package br.dev.matheus.vyta.dto;

import jakarta.validation.constraints.NotBlank;

public record ReceitaItemRequest(
        @NotBlank String nomeMedicamento,
        @NotBlank String dosagem,
        @NotBlank String quantidade,
        @NotBlank String duracaoTratamento
) {}
