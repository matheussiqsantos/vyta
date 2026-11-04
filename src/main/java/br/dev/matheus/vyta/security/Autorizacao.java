package br.dev.matheus.vyta.security;

import org.springframework.security.access.AccessDeniedException;

public final class Autorizacao {

    private Autorizacao() {}

    /** Garante que o {id} da URL é do próprio usuário logado. */
    public static void exigirMesmoUsuario(Long id, UsuarioLogado logado) {
        if (logado == null || id == null || !logado.id().equals(id)) {
            throw new AccessDeniedException("Acesso negado");
        }
    }

    public static void exigirTipo(UsuarioLogado logado, String tipo) {
        if (logado == null || !logado.tipo().equals(tipo)) {
            throw new AccessDeniedException("Acesso negado");
        }
    }
}