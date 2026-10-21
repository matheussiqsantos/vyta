package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.enums.TipoLaudo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public record LaudoRequest(
        @NotNull Long pacienteId,
        Long medicoSolicitanteId,
        Long consultaId,
        @NotBlank @Size(max = 255) String titulo,
        @NotNull TipoLaudo tipo,
        @NotBlank @Size(max = 255) String laboratorio,
        @NotNull @PastOrPresent LocalDate dataExame,
        @Size(max = 2000) String achados,
        @Size(max = 2000) String conclusao,
        @Size(max = 500) String arquivoUrl,
        List<@Valid LaudoResultadoRequest> resultados
) {}