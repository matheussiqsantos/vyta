package br.dev.matheus.vyta.model;

import br.dev.matheus.vyta.model.enums.TipoUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "medico",
       uniqueConstraints = @UniqueConstraint(columnNames = {"crm", "crm_uf"})
)
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Medico extends Usuario {
    
    @NotBlank
    @Size(max = 15)
    @Column(name = "crm", nullable = false, length = 15)
    private String crm;
    
    @NotBlank
    @Pattern(regexp = "[A-Z]{2}", message = "UF do CRM deve ter 2 letras maiúsculas")
    @Column(name = "crm_uf", nullable = false, length = 2, columnDefinition = "char(2)")
    private String crmUf;
    
    @Size(max = 100)
    @Column(name = "especialidade", length = 100)
    private String especialidade;
    
    public Medico() {
        setTipoUsuario(TipoUsuario.MEDICO);
    }
    
    @PrePersist
    @PreUpdate
    private void normalizar() {
        if (crmUf != null) crmUf = crmUf.trim().toUpperCase();
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getCrmUf() {
        return crmUf;
    }

    public void setCrmUf(String crmUf) {
        this.crmUf = crmUf;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
    
    
}
