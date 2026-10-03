package br.dev.matheus.vyta.model;

import br.dev.matheus.vyta.model.enums.TipoSanguineo;
import jakarta.persistence.*;
import br.dev.matheus.vyta.model.enums.TipoUsuario;


@Entity
@Table(name = "paciente")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Paciente extends Usuario {
    
   @Column(name = "tipo_sanguineo", length = 3)
   private TipoSanguineo tipoSanguineo;
   
   public Paciente() {
       setTipoUsuario(TipoUsuario.PACIENTE);
   }
   
   public TipoSanguineo getTipoSanguineo() {
       return tipoSanguineo;
   }
   
   public void setTipoSanguineo(TipoSanguineo tipoSanguineo) {
       this.tipoSanguineo = tipoSanguineo;
   }
}
