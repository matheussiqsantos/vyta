package br.dev.matheus.vyta.service;

import br.dev.matheus.vyta.dto.ReceitaItemRequest;
import br.dev.matheus.vyta.dto.ReceitaRequest;
import br.dev.matheus.vyta.dto.ReceitaResponse;
import br.dev.matheus.vyta.exception.RecursoNaoEncontradoException;
import br.dev.matheus.vyta.exception.RegraNegocioException;
import br.dev.matheus.vyta.model.Consulta;
import br.dev.matheus.vyta.model.Receita;
import br.dev.matheus.vyta.model.ReceitaItem;
import br.dev.matheus.vyta.model.enums.StatusConsulta;
import br.dev.matheus.vyta.model.enums.StatusReceita;
import br.dev.matheus.vyta.repository.ConsultaRepository;
import br.dev.matheus.vyta.repository.ReceitaRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final ConsultaRepository consultaRepository;

    public ReceitaService(ReceitaRepository receitaRepository,
                          ConsultaRepository consultaRepository) {
        this.receitaRepository = receitaRepository;
        this.consultaRepository = consultaRepository;
    }

    @Transactional
    public ReceitaResponse emitir(Long consultaId, ReceitaRequest req) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada."));

        if (!consulta.getMedico().getUsuarioId().equals(req.medicoId())) {
            throw new RegraNegocioException("Somente o médico da consulta pode emitir a receita.");
        }

        if (consulta.getStatusConsulta() != StatusConsulta.REALIZADA) {
            throw new RegraNegocioException("A receita só pode ser emitida para consultas realizadas.");
        }

        LocalDate hoje = LocalDate.now();
        if (req.dataVencimento().isBefore(hoje)) {
            throw new RegraNegocioException("A data de vencimento não pode estar no passado.");
        }

        boolean semNotificacao = req.numeroNotificacao() == null || req.numeroNotificacao().isBlank();
        if (req.tipoReceita().exigeNotificacao() && semNotificacao) {
            throw new RegraNegocioException("Esse tipo de receita exige o número de notificação.");
        }

        Receita receita = new Receita();
        receita.setConsulta(consulta);
        receita.setDataEmissao(hoje);
        receita.setDataVencimento(req.dataVencimento());
        receita.setTipoReceita(req.tipoReceita());
        receita.setStatusReceita(StatusReceita.ATIVA);
        receita.setNumeroNotificacao(semNotificacao ? null : req.numeroNotificacao().trim());

        for (ReceitaItemRequest ir : req.itens()) {
            ReceitaItem item = new ReceitaItem();
            item.setNomeMedicamento(ir.nomeMedicamento().trim());
            item.setDosagem(ir.dosagem().trim());
            item.setQuantidade(ir.quantidade().trim());
            item.setDuracaoTratamento(ir.duracaoTratamento().trim());
            receita.adicionarItem(item);
        }

        return ReceitaResponse.de(receitaRepository.save(receita));
    }

    @Transactional(readOnly = true)
    public ReceitaResponse buscarPorId(Long id) {
        return ReceitaResponse.de(receitaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Receita não encontrada.")));
    }

    @Transactional(readOnly = true)
    public List<ReceitaResponse> listarPorConsulta(Long consultaId) {
        if (!consultaRepository.existsById(consultaId)) {
            throw new RecursoNaoEncontradoException("Consulta não encontrada.");
        }
        return receitaRepository.findByConsultaIdOrderByDataEmissaoDesc(consultaId)
                .stream().map(ReceitaResponse::de).toList();
    }
}
