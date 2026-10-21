package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.enums.SexoBiologico;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record MedicoRequest(
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
        @NotBlank @Pattern(regexp = "\\d{4,10}", message = "CRM deve conter apenas números") String crm,
        @NotBlank @Pattern(regexp = "[A-Za-z]{2}", message = "UF deve ter 2 letras") String crmUf,
        @NotBlank @Size(max = 100) String especialidade
) {}
