package com.gestorcolegio.service.impl;

import com.gestorcolegio.config.JwtService;
import com.gestorcolegio.dto.AuthRequest;
import com.gestorcolegio.dto.AuthResponse;
import com.gestorcolegio.dto.RegisterRequest;
import com.gestorcolegio.entity.Usuario;
import com.gestorcolegio.entity.enums.Rol;
import com.gestorcolegio.repository.UsuarioRepository;
import com.gestorcolegio.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado post-autenticación"));

            String token = jwtService.generateToken(usuario.getUsername());

            return AuthResponse.builder()
                    .token(token)
                    .rol(usuario.getRol().name())
                    .build();

        } catch (Exception e) {
            System.err.println("Excepción en Login: " + e.getMessage());
            throw new RuntimeException("Error de autenticación: Credenciales inválidas");
        }
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(Rol.PROFESOR)
                .build();

        usuarioRepository.save(usuario);
        String token = jwtService.generateToken(usuario.getUsername());

        return new AuthResponse(token, usuario.getRol().name());
    }
}