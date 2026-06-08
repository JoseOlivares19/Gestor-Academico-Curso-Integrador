package com.gestorcolegio.repository;

import com.gestorcolegio.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    @Query("SELECT c FROM Curso c WHERE c.profesor.usuario.username = :username")
    List<Curso> findByProfesorUsername(@Param("username") String username);
}
