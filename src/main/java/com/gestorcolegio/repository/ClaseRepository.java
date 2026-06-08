package com.gestorcolegio.repository;

import com.gestorcolegio.entity.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClaseRepository extends JpaRepository<Clase, Long> {
    List<Clase> findByCursoId(Long cursoId);
}