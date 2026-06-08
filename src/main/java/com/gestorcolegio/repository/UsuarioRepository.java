package com.gestorcolegio.repository;

import com.gestorcolegio.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByUsername(String username);
    @Query("SELECT u FROM Usuario u WHERE u.rol = 'PROFESOR' AND NOT EXISTS " +
            "(SELECT p FROM Profesor p WHERE p.usuario.id = u.id)")
    List<Usuario> findUsuariosProfesorDisponibles();

}
