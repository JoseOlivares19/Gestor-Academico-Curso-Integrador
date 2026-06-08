package com.gestorcolegio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaseRequestDto {
    private LocalDate fecha;
    private String tema;
    private Long cursoId;
}
