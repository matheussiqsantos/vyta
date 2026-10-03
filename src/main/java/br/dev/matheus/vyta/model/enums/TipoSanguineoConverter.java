package br.dev.matheus.vyta.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoSanguineoConverter implements AttributeConverter<TipoSanguineo, String> {
        
    @Override
    public String convertToDatabaseColumn(TipoSanguineo tipo) {
        return tipo == null ? null : tipo.getDescricao();
    }
    
    @Override
    public TipoSanguineo convertToEntityAttribute(String valor) {
        return valor == null ? null : TipoSanguineo.fromDescricao(valor);
    }
}
