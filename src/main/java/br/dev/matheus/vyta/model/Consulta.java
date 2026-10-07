package br.dev.matheus.vyta.model;

import br.dev.matheus.vyta.model.enums.ModalidadeConsulta;
import br.dev.matheus.vyta.model.enums.StatusConsulta;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "consulta")
public class Consulta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultaId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;
    
    @Column(nullable = false)
    private LocalDate dataConsulta;
    
    @Column(nullable = false )
    private LocalTime horario;
    
    @Column(nullable = false, length = 100)
    private String tipoConsulta;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModalidadeConsulta modalidade;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConsulta statusConsulta;
    
    
    @Column(length = 100)
    private String convenio;
    
    // Local do atendimento: só preenchido em consultas presenciais
    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    public Consulta() {
    }

    public Long getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Long consultaId) {
        this.consultaId = consultaId;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public LocalDate getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDate dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public ModalidadeConsulta getModalidade() {
        return modalidade;
    }

    public void setModalidade(ModalidadeConsulta modalidade) {
        this.modalidade = modalidade;
    }

    public StatusConsulta getStatusConsulta() {
        return statusConsulta;
    }

    public void setStatusConsulta(StatusConsulta statusConsulta) {
        this.statusConsulta = statusConsulta;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
    
    
}
