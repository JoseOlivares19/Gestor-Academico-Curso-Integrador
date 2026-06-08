package com.gestorcolegio.controller;

import com.gestorcolegio.dto.ProfesorRequestDto;
import com.gestorcolegio.dto.ProfesorResponseDto;
import com.gestorcolegio.service.ProfesorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profesores")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProfesorController {

    private final ProfesorService profesorService;

    @PostMapping
    public ResponseEntity<ProfesorResponseDto> registrarProfesor(@RequestBody ProfesorRequestDto profesorDto) {

        ProfesorResponseDto profesor = profesorService.guardar(profesorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(profesor);
    }

    @GetMapping
    public ResponseEntity<List<ProfesorResponseDto>> listarProfesores() {
        List<ProfesorResponseDto> profesores = profesorService.listarProfesores();
        return ResponseEntity.status(HttpStatus.OK).body(profesores);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProfesor(@PathVariable Long id) {
        profesorService.eliminarProfesor(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProfesorResponseDto> actualizarProfesor(@PathVariable Long id, @RequestBody ProfesorRequestDto profesor) {
        ProfesorResponseDto profesoractualizado = profesorService.actualizarProfesor(id, profesor);
        return ResponseEntity.ok().body(profesoractualizado);
    }
}