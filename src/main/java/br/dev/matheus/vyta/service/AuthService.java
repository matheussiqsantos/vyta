package br.dev.matheus.vyta.service;

import br.dev.matheus.vyta.dto.LoginRequest;
import br.dev.matheus.vyta.dto.LoginResponse;
import br.dev.matheus.vyta.exception.CredenciaisInvalidasException;
import br.dev.matheus.vyta.model.Usuario;
import br.dev.matheus.vyta.model.enums.StatusConta;
import br.dev.matheus.vyta.repository.UsuarioRepository;
import br.dev.matheus.vyta.security.JwtService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final String hashFalso; 

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.hashFalso = passwordEncoder.encode("senha-falsa-para-igualar-tempo");
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest req) {
        String cpf = UsuarioRegras.soDigitos(req.cpf());
        Usuario u = usuarioRepository.findByCpf(cpf).orElse(null);

        boolean senhaOk = passwordEncoder.matches(req.senha(), u != null ? u.getSenha() : hashFalso);
        if (u == null || !senhaOk) {
            throw new CredenciaisInvalidasException();
        }
        if (u.getStatusConta() != StatusConta.ATIVA) { 
            throw new AccessDeniedException("Conta inativa");
        }
        return new LoginResponse(u.getUsuarioId(), u.getNome(), u.getTipoUsuario().name(), jwtService.gerar(u));
    }
}