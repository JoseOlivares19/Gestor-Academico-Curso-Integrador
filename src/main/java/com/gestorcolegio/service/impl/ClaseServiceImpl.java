package com.gestorcolegio.service.impl;

import com.gestorcolegio.dto.ClaseRequestDto;
import com.gestorcolegio.dto.ClaseResponseDto;
import com.gestorcolegio.entity.Clase;
import com.gestorcolegio.entity.Curso;
import com.gestorcolegio.repository.ClaseRepository;
import com.gestorcolegio.repository.CursoRepository;
import com.gestorcolegio.service.ClaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class ClaseServiceImpl implements ClaseService {
    private final ClaseRepository claseRepository;
    private final CursoRepository cursoRepository;

    @Override
    public ClaseResponseDto guardarClase(ClaseRequestDto clase){
        Curso curso = cursoRepository.findById(clase.getCursoId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        Clase claseGuardada = new Clase();
        claseGuardada.setTema(clase.getTema());
        claseGuardada.setFecha(clase.getFecha());
        claseGuardada.setCurso(curso);

        Clase claseSave = claseRepository.save(claseGuardada);
        return toDto(claseSave);
    }

    @Override
    public List<ClaseResponseDto> listarClases(){
        return claseRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }
    @Override
    public List<ClaseResponseDto> listarPorCurso(Long cursoId) {
        return claseRepository.findByCursoId(cursoId)
                .stream()
                .map(this::toDto)
                .toList();
    }
    @Override
    public void eliminarClase(Long id)
        {
        claseRepository.deleteById(id);

        }

    @Override
    public ClaseResponseDto actualizarClase(Long id, ClaseRequestDto clase){
        Clase claseActualizada = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        claseActualizada.setTema(clase.getTema());
        claseActualizada.setFecha(clase.getFecha());

        Clase actualizado = claseRepository.save(claseActualizada);
        return toDto(actualizado);
    }

    private ClaseResponseDto toDto(Clase clase) {
        ClaseResponseDto dto = new ClaseResponseDto();
        dto.setId(clase.getId());
        dto.setTema(clase.getTema());
        dto.setFecha(clase.getFecha());
        dto.setNombreCurso(clase.getCurso().getNombreCurso());
        return dto;
    }


}