package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.enums.SexoBiologico;
import br.dev.matheus.vyta.model.enums.TipoSanguineo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/** Edição de perfil: CPF e senha não mudam por aqui. */
public record PacienteUpdateRequest(
        @NotBlank @Size(max = 150) String nome,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 15) String telefone,
        @NotNull @Past LocalDate dataNascimento,
        @NotNull SexoBiologico sexoBiologico,
        @NotBlank @Size(max = 50) String identidadeGenero,
        @NotBlank @Size(max = 150) String nomeContatoEmergencia,
        @NotBlank @Size(max = 15) String telContatoEmergencia,
        Long enderecoId,
        @Valid EnderecoRequest endereco,
        TipoSanguineo tipoSanguineo
) {}