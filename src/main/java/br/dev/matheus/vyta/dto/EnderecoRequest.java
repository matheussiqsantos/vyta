package br.dev.matheus.vyta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoRequest(
    @NotBlank
    @Pattern(regexp = "\\d{8}", message = "CEP deve ter 8 dígitos, sem hífen")
    String cep,
        
    @NotBlank @Size(max = 255) String logradouro,
    
    @Size(max = 10) String numero,
    
    @Size(max = 150) String complemento,
    
    @NotBlank @Size(max = 50) String bairro,
    
    @NotBlank @Size(max = 50) String cidade,
    
    @NotBlank
    @Pattern(regexp = "[A-Za-z]{2}", message = "Estado deve ter 2 letras (ex: SP)")
    String estado,
    
    @Size(max = 50) String pais
) {}
