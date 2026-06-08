package com.gestorcolegio.service.impl;

import com.gestorcolegio.dto.AsistenciaRequestDto;
import com.gestorcolegio.dto.AsistenciaResponseDto;
import com.gestorcolegio.entity.Alumno;
import com.gestorcolegio.entity.Asistencia;
import com.gestorcolegio.entity.Clase;
import com.gestorcolegio.entity.enums.EstadoAsistencia; // Asegúrate de importar tu Enum
import com.gestorcolegio.repository.AlumnoRepository;
import com.gestorcolegio.repository.AsistenciaRepository;
import com.gestorcolegio.repository.ClaseRepository;
import com.gestorcolegio.service.AsistenciaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AsistenciaServiceImpl implements AsistenciaService {
    private final AsistenciaRepository asistenciaRepository;
    private final ClaseRepository claseRepository;
    private final AlumnoRepository alumnoRepository;

    @Override
    @Transactional
    public void guardarListaAsistencia(List<AsistenciaRequestDto> listaAsistencia) {
        listaAsistencia.forEach(this::guardarAsistencia);
    }

    @Override
    public AsistenciaResponseDto guardarAsistencia(AsistenciaRequestDto asistencia) {
        Alumno alumno = alumnoRepository.findById(asistencia.getAlumnoId())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        Clase clase = claseRepository.findById(asistencia.getClaseId())
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        Asistencia asistenciaGuardada = asistenciaRepository
                .findByAlumno_IdAndClase_Id(asistencia.getAlumnoId(), asistencia.getClaseId())
                .orElse(new Asistencia());

        asistenciaGuardada.setAlumno(alumno);
        asistenciaGuardada.setClase(clase);
        asistenciaGuardada.setEstado(EstadoAsistencia.valueOf(asistencia.getEstado()));

        return toDto(asistenciaRepository.save(asistenciaGuardada));
    }

    @Override
    public List<AsistenciaResponseDto> listarAsistencias() {
        return asistenciaRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }
    @Override
    public List<AsistenciaResponseDto> listarPorClase(Long claseId) {
        return asistenciaRepository.findByClaseId(claseId).stream()
                .map(this::toDto)
                .toList();
    }
    @Override
    public void eliminarAsistencia(Long id){
        asistenciaRepository.deleteById(id);
    }

    @Override
    public AsistenciaResponseDto actualizarAsistencia(Long id, AsistenciaRequestDto asistencia) {
        Asistencia asistenciaActualizada = asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));

        asistenciaActualizada.setEstado(EstadoAsistencia.valueOf(asistencia.getEstado()));
        Asistencia actualizada = asistenciaRepository.save(asistenciaActualizada);
        return toDto(actualizada);
    }

    private AsistenciaResponseDto toDto(Asistencia asistencia) {
        AsistenciaResponseDto dto = new AsistenciaResponseDto();
        dto.setId(asistencia.getId());
        dto.setNombreAlumno(asistencia.getAlumno().getNombreAlumno());
        dto.setEstado(asistencia.getEstado().name());

        dto.setAlumnoId(asistencia.getAlumno().getId());
        dto.setClaseId(asistencia.getClase().getId());

        return dto;
    }
}