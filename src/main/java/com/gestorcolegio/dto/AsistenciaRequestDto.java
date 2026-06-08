package com.gestorcolegio.dto;

import com.gestorcolegio.entity.enums.EstadoAsistencia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistenciaRequestDto {
    private Long alumnoId;
    private Long claseId;
    private String estado;
}
