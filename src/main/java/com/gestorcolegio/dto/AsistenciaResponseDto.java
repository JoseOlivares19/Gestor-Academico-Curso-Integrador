package com.gestorcolegio.dto;

import com.gestorcolegio.entity.enums.EstadoAsistencia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistenciaResponseDto {
    private Long id;
    private String nombreAlumno;
    private String estado;
    private Long alumnoId;
    private Long claseId;
}
