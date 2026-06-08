package com.gestorcolegio.controller;


import com.gestorcolegio.dto.CursoRequestDto;
import com.gestorcolegio.dto.CursoResponseDto;
import com.gestorcolegio.service.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cursos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CursoController {
private final CursoService cursoService;


    @PostMapping
    public ResponseEntity<CursoResponseDto> guardarCurso(@RequestBody CursoRequestDto cursoDto) {
        CursoResponseDto curso = cursoService.guardarCurso(cursoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(curso);
    }

    @GetMapping
    public ResponseEntity<List<CursoResponseDto>> listarCursos(){
        List<CursoResponseDto> cursos = cursoService.listarCursos();
        return ResponseEntity.status(HttpStatus.OK).body(cursos);
    }

    @GetMapping("/mis-cursos")

    public ResponseEntity<List<CursoResponseDto>> listarMisCursos(Principal principal) {
        return ResponseEntity.ok(cursoService.listarCursosPorProfesor(principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        cursoService.eliminarCurso(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDto> actualizarCurso(@PathVariable Long id, @RequestBody CursoRequestDto cursoDto) {
        CursoResponseDto curso = cursoService.actualizarCurso(id, cursoDto);
        return ResponseEntity.ok().body(curso);
    }
}
