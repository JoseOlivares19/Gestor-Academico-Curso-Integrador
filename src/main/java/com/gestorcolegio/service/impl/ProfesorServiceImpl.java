package com.gestorcolegio.service.impl;

import com.gestorcolegio.dto.ProfesorRequestDto;
import com.gestorcolegio.dto.ProfesorResponseDto;
import com.gestorcolegio.entity.Profesor;
import com.gestorcolegio.entity.Usuario;
import com.gestorcolegio.repository.ProfesorRepository;
import com.gestorcolegio.repository.UsuarioRepository;
import com.gestorcolegio.service.ProfesorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfesorServiceImpl implements ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public ProfesorResponseDto registrarProfesor(ProfesorRequestDto profesor){
        Profesor prof = new Profesor();
        prof.setNombreProfesor(profesor.getNombreProfesor());
        prof.setHabilitado(profesor.isHabilitado());

        Profesor profesorguardado = profesorRepository.save(prof);
        return toDto(profesorguardado);
    }

    @Override
    public List<ProfesorResponseDto> listarProfesores(){
        return profesorRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void eliminarProfesor(Long id){
        profesorRepository.deleteById(id);
    }

    @Override
    public ProfesorResponseDto actualizarProfesor(Long id, ProfesorRequestDto dto){
        Profesor profesorActualizado = profesorRepository.findById(id)
                .orElseThrow(() -> new ResolutionException("Error al encontrar el Profesor"));
        profesorActualizado.setNombreProfesor(dto.getNombreProfesor());
        profesorActualizado.setHabilitado(dto.isHabilitado());
        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            profesorActualizado.setUsuario(usuario);
        } else {
            profesorActualizado.setUsuario(null);
        }
        Profesor actualizado = profesorRepository.save(profesorActualizado);
        return toDto(actualizado);
    }

    @Override
    public ProfesorResponseDto guardar(ProfesorRequestDto dto) {
        Profesor profesor = new Profesor();
        profesor.setNombreProfesor(dto.getNombreProfesor());
        profesor.setHabilitado(dto.isHabilitado());

        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            profesor.setUsuario(usuario);
        }

        return toDto(profesorRepository.save(profesor));
    }

    private ProfesorResponseDto toDto(Profesor profesor) {
        ProfesorResponseDto dto = new ProfesorResponseDto();
        dto.setId(profesor.getId());
        dto.setNombreProfesor(profesor.getNombreProfesor());
        dto.setHabilitado(profesor.isHabilitado());
        if (profesor.getUsuario() != null) {
            dto.setUsername(profesor.getUsuario().getUsername());
        } else {
            dto.setUsername("Sin asignar");
        }

        return dto;
    }
}