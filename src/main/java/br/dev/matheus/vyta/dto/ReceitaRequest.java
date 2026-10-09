package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.enums.TipoReceita;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record ReceitaRequest(
        @NotNull Long medicoId,
        @NotNull TipoReceita tipoReceita,
        @NotNull LocalDate dataVencimento, 
        String numeroNotificacao,
        @NotEmpty @Valid List<ReceitaItemRequest> itens
) {}
