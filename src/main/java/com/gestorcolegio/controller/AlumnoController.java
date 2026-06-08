package com.gestorcolegio.controller;

import com.gestorcolegio.dto.AlumnoRequestDto;
import com.gestorcolegio.dto.AlumnoResponseDto;
import com.gestorcolegio.service.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/alumnos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AlumnoController {
    private final AlumnoService alumnoService;

    @PostMapping
    public ResponseEntity<AlumnoResponseDto> registrarAlumno(@RequestBody AlumnoRequestDto alumnoDto) {
        AlumnoResponseDto alumno = alumnoService.registrarAlumno(alumnoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(alumno);
    }

    @GetMapping
    public ResponseEntity<List<AlumnoResponseDto>> listarAlumnos(){
        List<AlumnoResponseDto> alumnos = alumnoService.listarAlumnos();
        return ResponseEntity.status(HttpStatus.OK).body(alumnos);
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<AlumnoResponseDto>> listarPorCurso(@PathVariable Long cursoId) {
        List<AlumnoResponseDto> alumnos = alumnoService.listarPorCurso(cursoId);
        return ResponseEntity.ok(alumnos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlumno(@PathVariable Long id){
        alumnoService.eliminarAlumno(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlumnoResponseDto> actualizarAlumno(@PathVariable Long id, @RequestBody AlumnoRequestDto alumnoDto){
        AlumnoResponseDto alumno = alumnoService.actualizarAlumno(id, alumnoDto);
        return ResponseEntity.ok().body(alumno);
    }

}
