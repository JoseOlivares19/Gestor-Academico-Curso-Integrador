package com.gestorcolegio.entity;

import com.gestorcolegio.entity.enums.EstadoAsistencia;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class Asistencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EstadoAsistencia estado;

    @ManyToOne
    @JoinColumn(name="alumno_id",nullable = false)
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name="clase_id",nullable = false)
    private Clase clase;
}
