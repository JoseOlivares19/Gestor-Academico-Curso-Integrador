package com.gestorcolegio.repository;

import com.gestorcolegio.entity.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlumnoRepository  extends JpaRepository<Alumno, Long> {
    List<Alumno> findByCursosId(Long cursoId);
}
