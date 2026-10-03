package br.dev.matheus.vyta.model;

import br.dev.matheus.vyta.model.enums.StatusConta;
import br.dev.matheus.vyta.model.enums.SexoBiologico;
import br.dev.matheus.vyta.model.enums.TipoUsuario;
import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long usuarioId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, unique = true, length = 15)
    private String cpf;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(length = 15)
    private String telefone;
    
    private LocalDate dataNascimento; 
    
    @Enumerated(EnumType.STRING)
    private SexoBiologico sexoBiologico;
    
    private String identidadeGenero;
    private String nomeContatoEmergencia;
    
    @Column(length = 15)
    private String telContatoEmergencia;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConta statusConta;
    
    @Column(nullable = false)
    private String senha;
    
    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;
    
    public Usuario() {
        
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public SexoBiologico getSexoBiologico() {
        return sexoBiologico;
    }

    public void setSexoBiologico(SexoBiologico sexoBiologico) {
        this.sexoBiologico = sexoBiologico;
    }

    public String getIdentidadeGenero() {
        return identidadeGenero;
    }

    public void setIdentidadeGenero(String identidadeGenero) {
        this.identidadeGenero = identidadeGenero;
    }

    public String getNomeContatoEmergencia() {
        return nomeContatoEmergencia;
    }

    public void setNomeContatoEmergencia(String nomeContatoEmergencia) {
        this.nomeContatoEmergencia = nomeContatoEmergencia;
    }

    public String getTelContatoEmergencia() {
        return telContatoEmergencia;
    }

    public void setTelContatoEmergencia(String telContatoEmergencia) {
        this.telContatoEmergencia = telContatoEmergencia;
    }

    public StatusConta getStatusConta() {
        return statusConta;
    }

    public void setStatusConta(StatusConta statusConta) {
        this.statusConta = statusConta;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
