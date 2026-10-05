package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.enums.SexoBiologico;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record MedicoRequest(
        @NotBlank @Size(max = 150) String nome,
        @NotBlank @Size(max = 15) String cpf,
        @NotBlank @Email String email,
        @Size(max = 15) String telefone,
        @Past LocalDate dataNascimento,
        SexoBiologico sexoBiologico,
        String identidadeGenero,
        String nomeContatoEmergencia,
        @Size(max = 15) String telContatoEmergencia,
        @NotBlank @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres") String senha,
        Long enderecoId,
        @Valid EnderecoRequest endereco,
        @NotBlank @Size(max = 15) String crm,
        @NotBlank @Pattern(regexp = "[A-Za-z]{2}", message = "UF deve ter 2 letras") String crmUf,
        @Size(max = 100) String especialidade
) {}
