package br.dev.matheus.vyta.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoSanguineoConverter implements AttributeConverter<TipoSanguineo, String> {

    @Override
    public String convertToDatabaseColumn(TipoSanguineo atributo) {
        return atributo == null ? null : atributo.getSimbolo();
    }

    @Override
    public TipoSanguineo convertToEntityAttribute(String coluna) {
        return coluna == null ? null : TipoSanguineo.fromSimbolo(coluna);
    }
}