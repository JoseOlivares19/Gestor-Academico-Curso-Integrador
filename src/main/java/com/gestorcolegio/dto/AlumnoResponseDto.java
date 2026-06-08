package com.gestorcolegio.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlumnoResponseDto {
    private Long id;
    private String nombre;
    private List<Long> cursosIds;
}
