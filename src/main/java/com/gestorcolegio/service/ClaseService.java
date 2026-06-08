package com.gestorcolegio.service;

import com.gestorcolegio.dto.ClaseRequestDto;
import com.gestorcolegio.dto.ClaseResponseDto;

import java.util.List;

public interface ClaseService {
    ClaseResponseDto guardarClase(ClaseRequestDto clase);
    List<ClaseResponseDto> listarClases();
    List<ClaseResponseDto> listarPorCurso(Long cursoId);
    void eliminarClase(Long id);
    ClaseResponseDto actualizarClase(Long id, ClaseRequestDto clase);
}
