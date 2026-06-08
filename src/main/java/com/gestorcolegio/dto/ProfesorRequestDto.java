package com.gestorcolegio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesorRequestDto {
    private String nombreProfesor;
    private boolean habilitado;
    private Long usuarioId;
}
