package com.gestorcolegio.service;

import com.gestorcolegio.dto.AlumnoRequestDto;
import com.gestorcolegio.dto.AlumnoResponseDto;
import com.gestorcolegio.entity.Alumno;

import java.util.List;

public interface AlumnoService {
    AlumnoResponseDto registrarAlumno(AlumnoRequestDto alumno);

    List<AlumnoResponseDto> listarAlumnos();

    List<AlumnoResponseDto> listarPorCurso(Long cursoId);

    void eliminarAlumno(Long id);

    AlumnoResponseDto actualizarAlumno(Long id, AlumnoRequestDto alumno);
}
