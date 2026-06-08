package com.gestorcolegio.service.impl;

import com.gestorcolegio.dto.CursoRequestDto;
import com.gestorcolegio.dto.CursoResponseDto;
import com.gestorcolegio.entity.Curso;
import com.gestorcolegio.entity.Profesor;
import com.gestorcolegio.exceptions.ResourceNotFoundException;
import com.gestorcolegio.repository.CursoRepository;
import com.gestorcolegio.repository.ProfesorRepository;
import com.gestorcolegio.service.CursoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final ProfesorRepository profesorRepository;


    @Override
    public CursoResponseDto guardarCurso(CursoRequestDto curso) {
        Curso cursoGuardado = new Curso();
        cursoGuardado.setNombreCurso(curso.getNombre());

        if (curso.getIdProfesor() != null) {
            Profesor profesor = profesorRepository.findById(curso.getIdProfesor())
                    .orElseThrow(() -> new RuntimeException("ERROR: Profesor no encontrado con ID: " + curso.getIdProfesor()));
            cursoGuardado.setProfesor(profesor);
        }

        Curso cursoSave = cursoRepository.save(cursoGuardado);
        return toDto(cursoSave);
    }

    @Override
    public List<CursoResponseDto> listarCursos() {

        return cursoRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<CursoResponseDto> listarCursosPorProfesor(String username) {
        return cursoRepository.findByProfesorUsername(username)
                .stream()
                .map(this::toDto)
                .toList();
    }
    @Override
    public void eliminarCurso(Long id) {

        cursoRepository.deleteById(id);
    }

    @Override
    public CursoResponseDto actualizarCurso(Long id, CursoRequestDto curso){
        Curso cursoActualizado = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado"));
        cursoActualizado.setNombreCurso(curso.getNombre());
        if (curso.getIdProfesor() != null) {
            Profesor profesor = profesorRepository.findById(curso.getIdProfesor())
                    .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado"));
            cursoActualizado.setProfesor(profesor);
        }
        Curso actualizado = cursoRepository.save(cursoActualizado);
        return toDto(actualizado);
    }

    private CursoResponseDto toDto(Curso curso) {
        CursoResponseDto dto = new CursoResponseDto();
        dto.setId(curso.getId());
        dto.setNombre(curso.getNombreCurso());

        if (curso.getProfesor() != null) {
            dto.setIdProfesor(curso.getProfesor().getId());
        } else {
            dto.setIdProfesor(null);
        }

        return dto;
    }
}


