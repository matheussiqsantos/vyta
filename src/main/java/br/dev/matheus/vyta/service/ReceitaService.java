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
import br.dev.matheus.vyta.model.enums.StatusConta;
import br.dev.matheus.vyta.model.enums.StatusReceita;
import br.dev.matheus.vyta.model.enums.TipoReceita;
import br.dev.matheus.vyta.repository.ConsultaRepository;
import br.dev.matheus.vyta.repository.ReceitaRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReceitaService {

    private static final ZoneId ZONA = ZoneId.of("America/Sao_Paulo");

    private final ReceitaRepository receitaRepository;
    private final ConsultaRepository consultaRepository;

    public ReceitaService(ReceitaRepository receitaRepository,
            ConsultaRepository consultaRepository) {
        this.receitaRepository = receitaRepository;
        this.consultaRepository = consultaRepository;
    }

    @Transactional
    public ReceitaResponse criar(ReceitaRequest req) {
        Consulta consulta = consultaRepository.findById(req.consultaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada"));

        if (consulta.getStatusConsulta() != StatusConsulta.REALIZADA) {
            throw new RegraNegocioException("A receita só pode ser emitida para consulta realizada");
        }
        if (consulta.getMedico().getStatusConta() != StatusConta.ATIVA) {
            throw new RegraNegocioException("Médico com conta inativa não pode emitir receitas");
        }
        if (consulta.getPaciente().getStatusConta() != StatusConta.ATIVA) {
            throw new RegraNegocioException("Paciente com conta inativa não pode receber receitas");
        }
        if (req.itens() == null || req.itens().isEmpty()) {
            throw new RegraNegocioException("A receita precisa ter pelo menos um item");
        }

        TipoReceita tipo = req.tipoReceita();
        String notificacao = req.numeroNotificacao() == null ? null : req.numeroNotificacao().trim();
        if (notificacao != null && notificacao.isEmpty()) {
            notificacao = null;
        }
        if (tipo.exigeNotificacao() && notificacao == null) {
            throw new RegraNegocioException("Este tipo de receita exige o número da notificação");
        }
        if (!tipo.exigeNotificacao() && notificacao != null) {
            throw new RegraNegocioException("Este tipo de receita não utiliza número de notificação");
        }

        LocalDate hoje = LocalDate.now(ZONA);

        Receita r = new Receita();
        r.setConsulta(consulta);
        r.setTipoReceita(tipo);
        r.setNumeroNotificacao(notificacao);
        r.setDataEmissao(hoje);
        r.setDataVencimento(hoje.plusDays(tipo.getDiasValidade()));
        r.setStatusReceita(StatusReceita.VALIDA);
        req.itens().forEach(i -> r.adicionarItem(montarItem(i)));

        return ReceitaResponse.de(receitaRepository.save(r));
    }

    @Transactional
    public ReceitaResponse adicionarItem(Long receitaId, ReceitaItemRequest req) {
        Receita r = buscarEntidade(receitaId);
        exigirValida(r);
        r.adicionarItem(montarItem(req));
        receitaRepository.saveAndFlush(r);
        return ReceitaResponse.de(r);
    }

    @Transactional
    public ReceitaResponse removerItem(Long receitaId, Long itemId) {
        Receita r = buscarEntidade(receitaId);
        exigirValida(r);
        if (r.getItens().size() <= 1) {
            throw new RegraNegocioException("Você não pode remover o último item da receita");
        }
        boolean removido = r.getItens().removeIf(i -> i.getId().equals(itemId));
        if (!removido) {
            throw new RecursoNaoEncontradoException("Item não encontrado nesta receita");
        }
        return ReceitaResponse.de(r);
    }

    @Transactional
    public ReceitaResponse buscarPorId(Long id) {
        return ReceitaResponse.de(buscarEntidade(id));
    }

    @Transactional
    public List<ReceitaResponse> listarPorConsulta(Long consultaId) {
        return receitaRepository.findByConsultaConsultaIdOrderByDataEmissaoDesc(consultaId).stream()
                .peek(this::sincronizarStatus)
                .map(ReceitaResponse::de)
                .toList();
    }

    @Scheduled(cron = "0 5 0 * * *", zone = "America/Sao_Paulo")
    @Transactional
    public void vencerReceitas() {
        receitaRepository.marcarVencidas(StatusReceita.VALIDA, StatusReceita.VENCIDA,
                LocalDate.now(ZONA));
    }

    private Receita buscarEntidade(Long id) {
        Receita r = receitaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Receita não encontrada"));
        sincronizarStatus(r);
        return r;
    }

    private void sincronizarStatus(Receita r) {
        if (r.getStatusReceita() == StatusReceita.VALIDA
                && r.getDataVencimento().isBefore(LocalDate.now(ZONA))) {
            r.setStatusReceita(StatusReceita.VENCIDA);
        }
    }

    private void exigirValida(Receita r) {
        if (r.getStatusReceita() != StatusReceita.VALIDA) {
            throw new RegraNegocioException("Receita vencida não pode ser alterada");
        }
    }

    private ReceitaItem montarItem(ReceitaItemRequest i) {
        ReceitaItem item = new ReceitaItem();
        item.setNomeMedicamento(i.nomeMedicamento().trim());
        item.setDosagem(i.dosagem().trim());
        item.setQuantidade(i.quantidade().trim());
        item.setDuracaoTratamento(i.duracaoTratamento().trim());
        return item;
    }

    @Transactional
    public List<ReceitaResponse> listarPorPaciente(Long pacienteId) {
        return receitaRepository.findByConsultaPacienteUsuarioIdOrderByDataEmissaoDesc(pacienteId).stream()
                .peek(this::sincronizarStatus)
                .map(ReceitaResponse::de)
                .toList();
    }
}
