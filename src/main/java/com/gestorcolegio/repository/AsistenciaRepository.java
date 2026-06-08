package com.gestorcolegio.repository;

import com.gestorcolegio.entity.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByClaseId(Long claseId);
    Optional<Asistencia> findByAlumno_IdAndClase_Id(Long alumnoId, Long claseId);
}
