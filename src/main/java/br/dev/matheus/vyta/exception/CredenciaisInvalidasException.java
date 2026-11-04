package br.dev.matheus.vyta.exception;

public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException() {
        super("CPF ou senha inválidos");
    }
}