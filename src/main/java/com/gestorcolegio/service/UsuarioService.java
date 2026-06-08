package com.gestorcolegio.service;

import com.gestorcolegio.dto.UsuarioResponseDto;
import java.util.List;

public interface UsuarioService {
    List<UsuarioResponseDto> listarUsuarios();
}