package br.dev.matheus.vyta.model;

import jakarta.persistence.*;

@Entity
@Table(name = "endereco")
public class Endereco {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enderecoId;
    
    @Column(nullable = false, length = 8)
    private String cep;
    
    @Column(nullable = false)
    private String logradouro;
    
    @Column(length = 10)
    private String numero;
    private String complemento;
    
    @Column(nullable = false)
    private String bairro;
    
    @Column(nullable = false)
    private String cidade;
    
    @Column(nullable = false, length = 2)
    private String estado;
    
    @Column(nullable = false)
    private String pais;
    
    public Endereco() {
        
        
    }

    public Long getEnderecoId() {
        return enderecoId;
    }

    public void setEnderecoId(Long enderecoId) {
        this.enderecoId = enderecoId;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
