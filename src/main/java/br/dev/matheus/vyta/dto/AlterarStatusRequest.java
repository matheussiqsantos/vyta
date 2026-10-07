package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.enums.StatusConsulta;
import jakarta.validation.constraints.NotNull;

public record AlterarStatusRequest(@NotNull StatusConsulta status) {}
