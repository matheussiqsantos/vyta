package br.dev.matheus.vyta.dto;

import br.dev.matheus.vyta.model.Endereco;

public record EnderecoResponse(
    Long id,
    String cep,
    String logradouro,
    String numero,
    String complemento,
    String bairro,
    String cidade,
    String estado,
    String pais
) {
    public static EnderecoResponse de(Endereco e) {
        return new EnderecoResponse(
                e.getEnderecoId(), e.getCep(), e.getLogradouro(), e.getNumero(),
                e.getComplemento(), e.getBairro(), e.getCidade(),
                e.getEstado(), e.getPais());
    }
}
