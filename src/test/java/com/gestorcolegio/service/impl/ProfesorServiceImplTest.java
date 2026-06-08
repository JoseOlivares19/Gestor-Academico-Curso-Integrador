package com.gestorcolegio.service.impl;

import com.gestorcolegio.dto.ProfesorRequestDto;
import com.gestorcolegio.dto.ProfesorResponseDto;
import com.gestorcolegio.entity.Profesor;
import com.gestorcolegio.entity.Usuario;
import com.gestorcolegio.repository.ProfesorRepository;
import com.gestorcolegio.repository.UsuarioRepository;
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
class ProfesorServiceImplTest {

    @Mock
    private ProfesorRepository profesorRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ProfesorServiceImpl profesorService;

    @Test
    void registrarProfesor_debeGuardarYRetornarDto() {
        ProfesorRequestDto dto = new ProfesorRequestDto();
        dto.setNombreProfesor("Carlos López");
        dto.setHabilitado(true);

        Profesor guardado = new Profesor();
        guardado.setId(1L);
        guardado.setNombreProfesor("Carlos López");
        guardado.setHabilitado(true);

        when(profesorRepository.save(any(Profesor.class))).thenReturn(guardado);

        ProfesorResponseDto resultado = profesorService.registrarProfesor(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombreProfesor()).isEqualTo("Carlos López");
        assertThat(resultado.isHabilitado()).isTrue();
        verify(profesorRepository, times(1)).save(any(Profesor.class));
    }

    @Test
    void listarProfesores_debeRetornarTodos() {
        Profesor p1 = new Profesor(); p1.setId(1L); p1.setNombreProfesor("Ana");
        Profesor p2 = new Profesor(); p2.setId(2L); p2.setNombreProfesor("Luis");

        when(profesorRepository.findAll()).thenReturn(List.of(p1, p2));

        List<ProfesorResponseDto> resultado = profesorService.listarProfesores();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombreProfesor()).isEqualTo("Ana");
    }

    @Test
    void eliminarProfesor_debeLlamarDeleteById() {
        doNothing().when(profesorRepository).deleteById(1L);

        profesorService.eliminarProfesor(1L);

        verify(profesorRepository, times(1)).deleteById(1L);
    }

    @Test
    void actualizarProfesor_cuandoExiste_sinUsuario_debeActualizar() {
        ProfesorRequestDto dto = new ProfesorRequestDto();
        dto.setNombreProfesor("Nombre Nuevo");
        dto.setHabilitado(false);
        dto.setUsuarioId(null);

        Profesor existente = new Profesor();
        existente.setId(1L);
        existente.setNombreProfesor("Nombre Viejo");

        Profesor actualizado = new Profesor();
        actualizado.setId(1L);
        actualizado.setNombreProfesor("Nombre Nuevo");
        actualizado.setHabilitado(false);

        when(profesorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(profesorRepository.save(any(Profesor.class))).thenReturn(actualizado);

        ProfesorResponseDto resultado = profesorService.actualizarProfesor(1L, dto);

        assertThat(resultado.getNombreProfesor()).isEqualTo("Nombre Nuevo");
        assertThat(resultado.isHabilitado()).isFalse();
    }

    @Test
    void actualizarProfesor_conUsuario_debeAsignarUsuario() {
        ProfesorRequestDto dto = new ProfesorRequestDto();
        dto.setNombreProfesor("María");
        dto.setHabilitado(true);
        dto.setUsuarioId(5L);

        Profesor existente = new Profesor(); existente.setId(1L);
        Usuario usuario = new Usuario(); usuario.setId(5L);

        Profesor actualizado = new Profesor();
        actualizado.setId(1L);
        actualizado.setNombreProfesor("María");
        actualizado.setHabilitado(true);
        actualizado.setUsuario(usuario);

        when(profesorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));
        when(profesorRepository.save(any(Profesor.class))).thenReturn(actualizado);

        ProfesorResponseDto resultado = profesorService.actualizarProfesor(1L, dto);

        assertThat(resultado.getNombreProfesor()).isEqualTo("María");
        verify(usuarioRepository).findById(5L);
    }

    @Test
    void actualizarProfesor_cuandoNoExiste_debeLanzarExcepcion() {
        when(profesorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(Exception.class,
                () -> profesorService.actualizarProfesor(99L, new ProfesorRequestDto()));

        verify(profesorRepository, never()).save(any());
    }

    @Test
    void guardar_conUsuario_debeAsignarUsuarioYGuardar() {
        ProfesorRequestDto dto = new ProfesorRequestDto();
        dto.setNombreProfesor("Roberto");
        dto.setHabilitado(true);
        dto.setUsuarioId(3L);

        Usuario usuario = new Usuario(); usuario.setId(3L);
        Profesor guardado = new Profesor();
        guardado.setId(10L);
        guardado.setNombreProfesor("Roberto");
        guardado.setHabilitado(true);
        guardado.setUsuario(usuario);

        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(usuario));
        when(profesorRepository.save(any(Profesor.class))).thenReturn(guardado);

        ProfesorResponseDto resultado = profesorService.guardar(dto);

        assertThat(resultado.getId()).isEqualTo(10L);
        verify(usuarioRepository).findById(3L);
        verify(profesorRepository).save(any(Profesor.class));
    }

    @Test
    void guardar_usuarioNoExiste_debeLanzarExcepcion() {
        ProfesorRequestDto dto = new ProfesorRequestDto();
        dto.setUsuarioId(99L);

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> profesorService.guardar(dto));
        verify(profesorRepository, never()).save(any());
    }
}