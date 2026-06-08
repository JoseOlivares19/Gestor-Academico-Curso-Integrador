package com.gestorcolegio.controller;


import com.gestorcolegio.dto.ClaseRequestDto;
import com.gestorcolegio.dto.ClaseResponseDto;
import com.gestorcolegio.service.ClaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clases")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ClaseController {
    private final ClaseService claseService;

    @PostMapping
    public ResponseEntity<ClaseResponseDto> guardarClase(@RequestBody ClaseRequestDto claseDto) {
        ClaseResponseDto clase = claseService.guardarClase(claseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clase);
    }

    @GetMapping
    public ResponseEntity<List<ClaseResponseDto>> listarClases() {
        List<ClaseResponseDto> clases = claseService.listarClases();
        return ResponseEntity.ok().body(clases);
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<ClaseResponseDto>> listarPorCurso(@PathVariable Long cursoId) {
        List<ClaseResponseDto> clases = claseService.listarPorCurso(cursoId);
        return ResponseEntity.ok(clases);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarClase(@PathVariable Long id) {
        claseService.eliminarClase(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClaseResponseDto> actualizarClase(@PathVariable Long id, @RequestBody ClaseRequestDto claseDto) {
        ClaseResponseDto clase = claseService.actualizarClase(id, claseDto);
        return ResponseEntity.ok().body(clase);
    }
}