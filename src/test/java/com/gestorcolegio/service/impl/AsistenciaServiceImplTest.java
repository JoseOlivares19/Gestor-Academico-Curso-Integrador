package com.gestorcolegio.service.impl;

import com.gestorcolegio.dto.AsistenciaRequestDto;
import com.gestorcolegio.dto.AsistenciaResponseDto;
import com.gestorcolegio.entity.Alumno;
import com.gestorcolegio.entity.Asistencia;
import com.gestorcolegio.entity.Clase;
import com.gestorcolegio.entity.enums.EstadoAsistencia;
import com.gestorcolegio.repository.AlumnoRepository;
import com.gestorcolegio.repository.AsistenciaRepository;
import com.gestorcolegio.repository.ClaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsistenciaServiceImplTest {

    @Mock
    private AsistenciaRepository asistenciaRepository;

    @Mock
    private ClaseRepository claseRepository;

    @Mock
    private AlumnoRepository alumnoRepository;

    @InjectMocks
    private AsistenciaServiceImpl asistenciaService;

    private Alumno alumnoBase() {
        Alumno a = new Alumno(); a.setId(1L); a.setNombreAlumno("Juan"); return a;
    }

    private Clase claseBase() {
        Clase c = new Clase(); c.setId(1L); return c;
    }

    @Test
    void guardarAsistencia_nueva_debeCrearYRetornarDto() {
        AsistenciaRequestDto dto = new AsistenciaRequestDto();
        dto.setAlumnoId(1L);
        dto.setClaseId(1L);
        dto.setEstado("PRESENTE");

        Alumno alumno = alumnoBase();
        Clase clase = claseBase();

        Asistencia guardada = new Asistencia();
        guardada.setId(1L);
        guardada.setAlumno(alumno);
        guardada.setClase(clase);
        guardada.setEstado(EstadoAsistencia.PRESENTE);

        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(claseRepository.findById(1L)).thenReturn(Optional.of(clase));
        when(asistenciaRepository.findByAlumno_IdAndClase_Id(1L, 1L)).thenReturn(Optional.empty());
        when(asistenciaRepository.save(any(Asistencia.class))).thenReturn(guardada);

        AsistenciaResponseDto resultado = asistenciaService.guardarAsistencia(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getEstado()).isEqualTo("PRESENTE");
        assertThat(resultado.getNombreAlumno()).isEqualTo("Juan");
    }

    @Test
    void guardarAsistencia_existente_debeActualizarEstado() {
        AsistenciaRequestDto dto = new AsistenciaRequestDto();
        dto.setAlumnoId(1L);
        dto.setClaseId(1L);
        dto.setEstado("AUSENTE");

        Alumno alumno = alumnoBase();
        Clase clase = claseBase();

        Asistencia existente = new Asistencia();
        existente.setId(5L);
        existente.setAlumno(alumno);
        existente.setClase(clase);
        existente.setEstado(EstadoAsistencia.PRESENTE);

        Asistencia actualizada = new Asistencia();
        actualizada.setId(5L);
        actualizada.setAlumno(alumno);
        actualizada.setClase(clase);
        actualizada.setEstado(EstadoAsistencia.AUSENTE);

        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(claseRepository.findById(1L)).thenReturn(Optional.of(clase));
        when(asistenciaRepository.findByAlumno_IdAndClase_Id(1L, 1L)).thenReturn(Optional.of(existente));
        when(asistenciaRepository.save(any(Asistencia.class))).thenReturn(actualizada);

        AsistenciaResponseDto resultado = asistenciaService.guardarAsistencia(dto);

        assertThat(resultado.getEstado()).isEqualTo("AUSENTE");
    }

    @Test
    void guardarAsistencia_alumnoNoExiste_debeLanzarExcepcion() {
        AsistenciaRequestDto dto = new AsistenciaRequestDto();
        dto.setAlumnoId(99L);
        dto.setClaseId(1L);

        when(alumnoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> asistenciaService.guardarAsistencia(dto));
        verify(asistenciaRepository, never()).save(any());
    }

    @Test
    void guardarAsistencia_claseNoExiste_debeLanzarExcepcion() {
        AsistenciaRequestDto dto = new AsistenciaRequestDto();
        dto.setAlumnoId(1L);
        dto.setClaseId(99L);

        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumnoBase()));
        when(claseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> asistenciaService.guardarAsistencia(dto));
        verify(asistenciaRepository, never()).save(any());
    }

    @Test
    void listarAsistencias_debeRetornarTodas() {
        Alumno alumno = alumnoBase();
        Clase clase = claseBase();

        Asistencia a1 = new Asistencia(); a1.setId(1L); a1.setAlumno(alumno);
        a1.setClase(clase); a1.setEstado(EstadoAsistencia.PRESENTE);

        when(asistenciaRepository.findAll()).thenReturn(List.of(a1));

        List<AsistenciaResponseDto> resultado = asistenciaService.listarAsistencias();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEstado()).isEqualTo("PRESENTE");
    }

    @Test
    void listarPorClase_debeRetornarAsistenciasDeEsaClase() {
        Alumno alumno = alumnoBase();
        Clase clase = claseBase();

        Asistencia a = new Asistencia(); a.setId(1L); a.setAlumno(alumno);
        a.setClase(clase); a.setEstado(EstadoAsistencia.AUSENTE);

        when(asistenciaRepository.findByClaseId(1L)).thenReturn(List.of(a));

        List<AsistenciaResponseDto> resultado = asistenciaService.listarPorClase(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getClaseId()).isEqualTo(1L);
    }

    @Test
    void eliminarAsistencia_debeLlamarDeleteById() {
        doNothing().when(asistenciaRepository).deleteById(1L);

        asistenciaService.eliminarAsistencia(1L);

        verify(asistenciaRepository, times(1)).deleteById(1L);
    }

    @Test
    void actualizarAsistencia_cuandoExiste_debeActualizarEstado() {
        AsistenciaRequestDto dto = new AsistenciaRequestDto();
        dto.setEstado("TARDANZA");

        Alumno alumno = alumnoBase();
        Clase clase = claseBase();

        Asistencia existente = new Asistencia();
        existente.setId(1L);
        existente.setAlumno(alumno);
        existente.setClase(clase);
        existente.setEstado(EstadoAsistencia.PRESENTE);

        Asistencia actualizada = new Asistencia();
        actualizada.setId(1L);
        actualizada.setAlumno(alumno);
        actualizada.setClase(clase);
        actualizada.setEstado(EstadoAsistencia.TARDANZA);

        when(asistenciaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(asistenciaRepository.save(any(Asistencia.class))).thenReturn(actualizada);

        AsistenciaResponseDto resultado = asistenciaService.actualizarAsistencia(1L, dto);

        assertThat(resultado.getEstado()).isEqualTo("TARDANZA");
    }

    @Test
    void actualizarAsistencia_cuandoNoExiste_debeLanzarExcepcion() {
        when(asistenciaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> asistenciaService.actualizarAsistencia(99L, new AsistenciaRequestDto()));

        verify(asistenciaRepository, never()).save(any());
    }
}