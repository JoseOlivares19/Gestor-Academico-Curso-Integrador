package com.gestorcolegio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaseResponseDto {
    private Long id;
    private String tema;
    private String nombreCurso;
    private LocalDate fecha;
}
