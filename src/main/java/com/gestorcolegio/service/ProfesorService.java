package com.gestorcolegio.service;

import com.gestorcolegio.dto.ProfesorRequestDto;
import com.gestorcolegio.dto.ProfesorResponseDto;
import com.gestorcolegio.entity.Profesor;

import java.util.List;

public interface ProfesorService {
    ProfesorResponseDto registrarProfesor(ProfesorRequestDto profesor);
    List<ProfesorResponseDto> listarProfesores();
    void eliminarProfesor(Long id);
    ProfesorResponseDto guardar(ProfesorRequestDto dto);
    ProfesorResponseDto actualizarProfesor(Long id, ProfesorRequestDto profesor);
}
