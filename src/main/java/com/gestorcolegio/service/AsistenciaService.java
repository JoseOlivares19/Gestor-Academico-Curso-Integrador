package com.gestorcolegio.service;

import com.gestorcolegio.dto.AsistenciaRequestDto;
import com.gestorcolegio.dto.AsistenciaResponseDto;

import java.util.List;

public interface AsistenciaService {
    AsistenciaResponseDto guardarAsistencia(AsistenciaRequestDto asistencia);
    List<AsistenciaResponseDto> listarAsistencias();
    List<AsistenciaResponseDto> listarPorClase(Long claseId);
    void eliminarAsistencia(Long id);
    AsistenciaResponseDto actualizarAsistencia(Long id, AsistenciaRequestDto asistencia);
    void guardarListaAsistencia(List<AsistenciaRequestDto> listaDto);
}
