package br.dev.matheus.vyta.security;

/** Principal guardado no SecurityContext após validar o JWT. tipo = "PACIENTE" ou "MEDICO". */
public record UsuarioLogado(Long id, String tipo) {}