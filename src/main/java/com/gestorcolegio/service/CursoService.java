package com.gestorcolegio.service;

import com.gestorcolegio.dto.CursoRequestDto;
import com.gestorcolegio.dto.CursoResponseDto;
import java.util.List;

public interface CursoService {
    CursoResponseDto guardarCurso(CursoRequestDto curso);
    List<CursoResponseDto> listarCursos();
    List<CursoResponseDto> listarCursosPorProfesor(String username);
    void eliminarCurso(Long id);
    CursoResponseDto actualizarCurso(Long id, CursoRequestDto curso);
}
