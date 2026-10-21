package br.dev.matheus.vyta.service;

import br.dev.matheus.vyta.dto.EnderecoRequest;
import br.dev.matheus.vyta.exception.RegraNegocioException;
import br.dev.matheus.vyta.repository.UsuarioRepository;
import br.dev.matheus.vyta.util.CpfValidator;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.stereotype.Component;

@Component
public class UsuarioRegras {

    public record DadosNormalizados(String cpf, String email, String telefone, String telEmergencia) {}

    private final UsuarioRepository usuarioRepository;

    public UsuarioRegras(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static String soDigitos(String valor) {
        return valor == null ? null : valor.replaceAll("\\D", "");
    }

    public DadosNormalizados validarNovo(String cpfBruto, String emailBruto, String telefoneBruto,
                                         String telEmergenciaBruto, LocalDate nascimento,
                                         int idadeMinima, Long enderecoId, EnderecoRequest endereco) {

        String cpf = soDigitos(cpfBruto);
        String email = emailBruto.trim().toLowerCase();
        String telefone = soDigitos(telefoneBruto);
        String telEmergencia = soDigitos(telEmergenciaBruto);

        if (!CpfValidator.isValido(cpf)) {
            throw new RegraNegocioException("CPF inválido");
        }
        validarTelefone(telefone, "Telefone");
        validarTelefone(telEmergencia, "Telefone do contato de emergência");

        if (telefone.equals(telEmergencia)) {
            throw new RegraNegocioException("O contato de emergência deve ter um telefone diferente do seu");
        }

        validarNascimento(nascimento, idadeMinima);

        if (enderecoId != null && endereco != null) {
            throw new RegraNegocioException("Informe enderecoId ou endereco, não os dois");
        }

        if (usuarioRepository.existsByCpf(cpf)) {
            throw new RegraNegocioException("CPF já cadastrado");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new RegraNegocioException("E-mail já cadastrado");
        }
        if (usuarioRepository.existsByTelefone(telefone)) {
            throw new RegraNegocioException("Telefone já cadastrado");
        }

        return new DadosNormalizados(cpf, email, telefone, telEmergencia);
    }

    private void validarTelefone(String digitos, String campo) {
        // DDD + número: 10 (fixo) ou 11 dígitos (celular)
        if (digitos == null || !digitos.matches("\\d{10,11}")) {
            throw new RegraNegocioException(campo + " inválido. Use DDD + número");
        }
    }

    private void validarNascimento(LocalDate nascimento, int idadeMinima) {
        int idade = Period.between(nascimento, LocalDate.now()).getYears();
        if (idade < idadeMinima) {
            throw new RegraNegocioException("Idade mínima não atingida: " + idadeMinima + " anos");
        }
        if (idade > 120) {
            throw new RegraNegocioException("Data de nascimento inválida");
        }
    }
}