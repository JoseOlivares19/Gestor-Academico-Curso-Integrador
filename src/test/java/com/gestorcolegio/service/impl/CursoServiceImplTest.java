package com.gestorcolegio.service.impl;

import com.gestorcolegio.dto.CursoRequestDto;
import com.gestorcolegio.dto.CursoResponseDto;
import com.gestorcolegio.entity.Curso;
import com.gestorcolegio.entity.Profesor;
import com.gestorcolegio.exceptions.ResourceNotFoundException;
import com.gestorcolegio.repository.CursoRepository;
import com.gestorcolegio.repository.ProfesorRepository;
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
class CursoServiceImplTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private ProfesorRepository profesorRepository;

    @InjectMocks
    private CursoServiceImpl cursoService;

    @Test
    void guardarCurso_sinProfesor_debeGuardarYRetornarDto() {
        CursoRequestDto dto = new CursoRequestDto();
        dto.setNombre("Matemáticas");
        dto.setIdProfesor(null);

        Curso cursoGuardado = new Curso();
        cursoGuardado.setId(1L);
        cursoGuardado.setNombreCurso("Matemáticas");

        when(cursoRepository.save(any(Curso.class))).thenReturn(cursoGuardado);

        CursoResponseDto resultado = cursoService.guardarCurso(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Matemáticas");
        verify(cursoRepository, times(1)).save(any(Curso.class));
        verifyNoInteractions(profesorRepository);
    }

    @Test
    void guardarCurso_conProfesor_debeAsignarProfesor() {
        CursoRequestDto dto = new CursoRequestDto();
        dto.setNombre("Historia");
        dto.setIdProfesor(1L);

        Profesor profesor = new Profesor();
        profesor.setId(1L);

        Curso cursoGuardado = new Curso();
        cursoGuardado.setId(2L);
        cursoGuardado.setNombreCurso("Historia");
        cursoGuardado.setProfesor(profesor);

        when(profesorRepository.findById(1L)).thenReturn(Optional.of(profesor));
        when(cursoRepository.save(any(Curso.class))).thenReturn(cursoGuardado);

        CursoResponseDto resultado = cursoService.guardarCurso(dto);

        assertThat(resultado.getIdProfesor()).isEqualTo(1L);
        verify(profesorRepository).findById(1L);
    }

    @Test
    void guardarCurso_profesorNoExiste_debeLanzarExcepcion() {
        CursoRequestDto dto = new CursoRequestDto();
        dto.setNombre("Física");
        dto.setIdProfesor(99L);

        when(profesorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cursoService.guardarCurso(dto));
        verify(cursoRepository, never()).save(any());
    }

    @Test
    void listarCursos_debeRetornarTodos() {
        Curso c1 = new Curso(); c1.setId(1L); c1.setNombreCurso("Arte");
        Curso c2 = new Curso(); c2.setId(2L); c2.setNombreCurso("Música");

        when(cursoRepository.findAll()).thenReturn(List.of(c1, c2));

        List<CursoResponseDto> resultado = cursoService.listarCursos();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Arte");
    }

    @Test
    void listarCursosPorProfesor_debeRetornarCursosDelProfesor() {
        Curso curso = new Curso(); curso.setId(1L); curso.setNombreCurso("Biología");

        when(cursoRepository.findByProfesorUsername("prof1")).thenReturn(List.of(curso));

        List<CursoResponseDto> resultado = cursoService.listarCursosPorProfesor("prof1");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Biología");
    }

    @Test
    void eliminarCurso_debeLlamarDeleteById() {
        doNothing().when(cursoRepository).deleteById(1L);

        cursoService.eliminarCurso(1L);

        verify(cursoRepository, times(1)).deleteById(1L);
    }

    @Test
    void actualizarCurso_cuandoExiste_debeActualizar() {
        CursoRequestDto dto = new CursoRequestDto();
        dto.setNombre("Química");
        dto.setIdProfesor(null);

        Curso existente = new Curso(); existente.setId(1L); existente.setNombreCurso("Viejo");
        Curso actualizado = new Curso(); actualizado.setId(1L); actualizado.setNombreCurso("Química");

        when(cursoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(cursoRepository.save(any(Curso.class))).thenReturn(actualizado);

        CursoResponseDto resultado = cursoService.actualizarCurso(1L, dto);

        assertThat(resultado.getNombre()).isEqualTo("Química");
    }

    @Test
    void actualizarCurso_cuandoNoExiste_debeLanzarExcepcion() {
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> cursoService.actualizarCurso(99L, new CursoRequestDto()));

        verify(cursoRepository, never()).save(any());
    }
}