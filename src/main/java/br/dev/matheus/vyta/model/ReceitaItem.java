package br.dev.matheus.vyta.model;

import jakarta.persistence.*;

@Entity
@Table(name = "receita_item")
public class ReceitaItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receita_item_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receita_id", nullable = false)
    private Receita receita;
    
    @Column(name = "nome_medicamento", nullable = false)
    private String nomeMedicamento;
    
    @Column(nullable = false)
    private String dosagem;
    
    @Column(nullable = false)
    private String quantidade;
    
    @Column(name = "duracao_tratamento", nullable = false)
    private String duracaoTratamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Receita getReceita() {
        return receita;
    }

    public void setReceita(Receita receita) {
        this.receita = receita;
    }

    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getDuracaoTratamento() {
        return duracaoTratamento;
    }

    public void setDuracaoTratamento(String duracaoTratamento) {
        this.duracaoTratamento = duracaoTratamento;
    }
    
    
}
