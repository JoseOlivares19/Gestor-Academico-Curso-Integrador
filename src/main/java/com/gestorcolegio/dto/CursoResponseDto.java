package com.gestorcolegio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoResponseDto {
    private Long id;
    private String nombre;
    private Long idProfesor;
}
