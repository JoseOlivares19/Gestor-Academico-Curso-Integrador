package com.gestorcolegio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesorResponseDto {
    private Long id;
    private String nombreProfesor;
    private boolean habilitado;
    private String username;
}
