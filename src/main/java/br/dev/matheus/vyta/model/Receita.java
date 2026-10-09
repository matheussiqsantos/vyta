package br.dev.matheus.vyta.model;

import br.dev.matheus.vyta.model.enums.StatusReceita;
import br.dev.matheus.vyta.model.enums.TipoReceita;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "receita")
public class Receita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receita_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;
    
    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;
    
    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;
    
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "tipo_receita", nullable = false)
    private TipoReceita tipoReceita;
    
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status_receita", nullable = false)
    private StatusReceita statusReceita;
    
    @Column(name = "numero_notificacao")
    private String numeroNotificacao;
    
    @OneToMany(mappedBy = "receita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceitaItem> itens = new ArrayList<>();
    
    public void adicionarItem(ReceitaItem item) {
        itens.add(item);
        item.setReceita(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public TipoReceita getTipoReceita() {
        return tipoReceita;
    }

    public void setTipoReceita(TipoReceita tipoReceita) {
        this.tipoReceita = tipoReceita;
    }

    public StatusReceita getStatusReceita() {
        return statusReceita;
    }

    public void setStatusReceita(StatusReceita statusReceita) {
        this.statusReceita = statusReceita;
    }

    public String getNumeroNotificacao() {
        return numeroNotificacao;
    }

    public void setNumeroNotificacao(String numeroNotificacao) {
        this.numeroNotificacao = numeroNotificacao;
    }

    public List<ReceitaItem> getItens() {
        return itens;
    }

    public void setItens(List<ReceitaItem> itens) {
        this.itens = itens;
    }
    
     
     
}
