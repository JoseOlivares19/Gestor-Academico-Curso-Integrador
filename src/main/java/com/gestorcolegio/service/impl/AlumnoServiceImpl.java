package com.gestorcolegio.service.impl;

import com.gestorcolegio.dto.AlumnoRequestDto;
import com.gestorcolegio.dto.AlumnoResponseDto;
import com.gestorcolegio.entity.Alumno;
import com.gestorcolegio.entity.Curso;
import com.gestorcolegio.exceptions.ResourceNotFoundException;
import com.gestorcolegio.repository.AlumnoRepository;
import com.gestorcolegio.repository.CursoRepository;
import com.gestorcolegio.service.AlumnoService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final CursoRepository cursoRepository;

    @Override
    public AlumnoResponseDto registrarAlumno(AlumnoRequestDto alumnoDto) {
        Alumno alumno = new Alumno();
        alumno.setNombreAlumno(alumnoDto.getNombre());

        if (alumnoDto.getCursosIds() != null && !alumnoDto.getCursosIds().isEmpty()) {
            List<Curso> cursos = cursoRepository.findAllById(alumnoDto.getCursosIds());
            alumno.setCursos(cursos);
        }
        Alumno alumnoGuardado = alumnoRepository.save(alumno);
        return toDto(alumnoGuardado);
    }

    @Override
    public List<AlumnoResponseDto> listarAlumnos() {
        return alumnoRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }
    @Override
    public List<AlumnoResponseDto> listarPorCurso(Long cursoId) {
        List<Alumno> alumnos = alumnoRepository.findByCursosId(cursoId);

        return alumnos.stream()
                .map(this::toDto)
                .toList();
    }
    @Override
    public void eliminarAlumno(Long id) {
        alumnoRepository.deleteById(id);
    }

    @Override
    public AlumnoResponseDto actualizarAlumno(Long id, AlumnoRequestDto dto) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado"));
        alumno.setNombreAlumno(dto.getNombre());

        if (dto.getCursosIds() != null) {
            List<Curso> cursos = cursoRepository.findAllById(dto.getCursosIds());
            alumno.setCursos(cursos);
        }

        Alumno actualizado = alumnoRepository.save(alumno);
        return toDto(actualizado);
    }

    private AlumnoResponseDto toDto(Alumno alumno) {
        AlumnoResponseDto dto = new AlumnoResponseDto();
        dto.setId(alumno.getId());
        dto.setNombre(alumno.getNombreAlumno());
        if (alumno.getCursos() != null) {
            List<Long> cursosIds = alumno.getCursos()
                    .stream()
                    .map(Curso::getId)
                    .toList();
            dto.setCursosIds(cursosIds);
        }
        return dto;
    }
}


