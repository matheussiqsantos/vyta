package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.Laudo;
import br.dev.matheus.vyta.model.enums.TipoLaudo;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record LaudoResponse(
        Long id, Long pacienteId, Long medicoSolicitanteId, Long consultaId,
        String titulo, TipoLaudo tipo, String laboratorio, LocalDate dataExame,
        String achados, String conclusao, String arquivoUrl, OffsetDateTime criadoEm,
        List<LaudoResultadoResponse> resultados) {

    public static LaudoResponse de(Laudo l) {
        return new LaudoResponse(
                l.getId(),
                l.getPaciente().getUsuarioId(),
                l.getMedicoSolicitante() == null ? null : l.getMedicoSolicitante().getUsuarioId(),
                l.getConsulta() == null ? null : l.getConsulta().getConsultaId(),
                l.getTitulo(), l.getTipo(), l.getLaboratorio(), l.getDataExame(),
                l.getAchados(), l.getConclusao(), l.getArquivoUrl(), l.getCriadoEm(),
                l.getResultados().stream().map(LaudoResultadoResponse::de).toList());
    }
}