package com.gestorcolegio.service.impl;

import com.gestorcolegio.dto.AlumnoRequestDto;
import com.gestorcolegio.dto.AlumnoResponseDto;
import com.gestorcolegio.entity.Alumno;
import com.gestorcolegio.entity.Curso;
import com.gestorcolegio.exceptions.ResourceNotFoundException;
import com.gestorcolegio.repository.AlumnoRepository;
import com.gestorcolegio.repository.CursoRepository;
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
class AlumnoServiceImplTest {

    @Mock
    private AlumnoRepository alumnoRepository;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private AlumnoServiceImpl alumnoService;


    @Test
    void registrarAlumno_sinCursos_debeGuardarYRetornarDto() {
        AlumnoRequestDto dto = new AlumnoRequestDto();
        dto.setNombre("Juan Pérez");
        dto.setCursosIds(null);

        Alumno alumnoGuardado = new Alumno();
        alumnoGuardado.setId(1L);
        alumnoGuardado.setNombreAlumno("Juan Pérez");

        when(alumnoRepository.save(any(Alumno.class))).thenReturn(alumnoGuardado);

        AlumnoResponseDto resultado = alumnoService.registrarAlumno(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Juan Pérez");

        verify(alumnoRepository, times(1)).save(any(Alumno.class));
    }

    @Test
    void registrarAlumno_conCursos_debeAsignarCursos() {

        AlumnoRequestDto dto = new AlumnoRequestDto();
        dto.setNombre("Ana García");
        dto.setCursosIds(List.of(1L, 2L));

        Curso curso1 = new Curso(); curso1.setId(1L);
        Curso curso2 = new Curso(); curso2.setId(2L);

        Alumno alumnoGuardado = new Alumno();
        alumnoGuardado.setId(10L);
        alumnoGuardado.setNombreAlumno("Ana García");
        alumnoGuardado.setCursos(List.of(curso1, curso2));

        when(cursoRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(curso1, curso2));
        when(alumnoRepository.save(any(Alumno.class))).thenReturn(alumnoGuardado);

        AlumnoResponseDto resultado = alumnoService.registrarAlumno(dto);

        assertThat(resultado.getCursosIds()).containsExactly(1L, 2L);
        verify(cursoRepository, times(1)).findAllById(List.of(1L, 2L));
    }

    @Test
    void listarAlumnos_debeRetornarListaCompleta() {

        Alumno a1 = new Alumno(); a1.setId(1L); a1.setNombreAlumno("Juan");
        Alumno a2 = new Alumno(); a2.setId(2L); a2.setNombreAlumno("María");

        when(alumnoRepository.findAll()).thenReturn(List.of(a1, a2));

        List<AlumnoResponseDto> resultado = alumnoService.listarAlumnos();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Juan");
        assertThat(resultado.get(1).getNombre()).isEqualTo("María");
    }

    @Test
    void listarAlumnos_cuandoNoHayAlumnos_debeRetornarListaVacia() {
        when(alumnoRepository.findAll()).thenReturn(List.of());

        List<AlumnoResponseDto> resultado = alumnoService.listarAlumnos();

        assertThat(resultado).isEmpty();
    }

    @Test
    void listarPorCurso_debeRetornarSoloAlumnosDeEseCurso() {
        Long cursoId = 5L;
        Alumno alumno = new Alumno();
        alumno.setId(1L);
        alumno.setNombreAlumno("Carlos");

        when(alumnoRepository.findByCursosId(cursoId)).thenReturn(List.of(alumno));

        List<AlumnoResponseDto> resultado = alumnoService.listarPorCurso(cursoId);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Carlos");
        verify(alumnoRepository).findByCursosId(cursoId);
    }

    @Test
    void eliminarAlumno_debeLlamarDeleteById() {

        Long id = 1L;

        doNothing().when(alumnoRepository).deleteById(id);

        alumnoService.eliminarAlumno(id);
        verify(alumnoRepository, times(1)).deleteById(id);
    }

    @Test
    void actualizarAlumno_cuandoExiste_debeActualizarYRetornarDto() {
        Long id = 1L;
        AlumnoRequestDto dto = new AlumnoRequestDto();
        dto.setNombre("Nombre Actualizado");
        dto.setCursosIds(null);

        Alumno alumnoExistente = new Alumno();
        alumnoExistente.setId(id);
        alumnoExistente.setNombreAlumno("Nombre Viejo");

        Alumno alumnoActualizado = new Alumno();
        alumnoActualizado.setId(id);
        alumnoActualizado.setNombreAlumno("Nombre Actualizado");

        when(alumnoRepository.findById(id)).thenReturn(Optional.of(alumnoExistente));
        when(alumnoRepository.save(any(Alumno.class))).thenReturn(alumnoActualizado);

        AlumnoResponseDto resultado = alumnoService.actualizarAlumno(id, dto);

        assertThat(resultado.getNombre()).isEqualTo("Nombre Actualizado");
        verify(alumnoRepository).findById(id);
        verify(alumnoRepository).save(any(Alumno.class));
    }

    @Test
    void actualizarAlumno_cuandoNoExiste_debeLanzarResourceNotFoundException() {
        Long id = 99L;
        AlumnoRequestDto dto = new AlumnoRequestDto();

        when(alumnoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> alumnoService.actualizarAlumno(id, dto));

        verify(alumnoRepository, never()).save(any());
    }
}