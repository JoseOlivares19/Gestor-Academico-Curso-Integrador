package com.gestorcolegio.service.impl;

import com.gestorcolegio.dto.UsuarioResponseDto;
import com.gestorcolegio.entity.Usuario;
import com.gestorcolegio.repository.UsuarioRepository;
import com.gestorcolegio.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public List<UsuarioResponseDto> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    private UsuarioResponseDto toDto(Usuario usuario) {
        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());

        if (usuario.getRol() != null) {
            dto.setRol(usuario.getRol().toString());
        }

        return dto;
    }
}