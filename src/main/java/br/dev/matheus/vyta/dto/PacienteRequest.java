package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.enums.SexoBiologico;
import br.dev.matheus.vyta.model.enums.TipoSanguineo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record PacienteRequest(
        @NotBlank @Size(max = 150) String nome,
        @NotBlank @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}", message = "CPF em formato inválido") String cpf,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 15) String telefone,
        @NotNull @Past LocalDate dataNascimento,
        @NotNull SexoBiologico sexoBiologico,
        @NotBlank @Size(max = 50) String identidadeGenero,
        @NotBlank @Size(max = 150) String nomeContatoEmergencia,
        @NotBlank @Size(max = 15) String telContatoEmergencia,
        @NotBlank @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres") String senha,
        Long enderecoId,
        @Valid EnderecoRequest endereco,
        TipoSanguineo tipoSanguineo
) {}
