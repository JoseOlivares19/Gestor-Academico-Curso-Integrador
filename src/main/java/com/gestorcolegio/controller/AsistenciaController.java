package com.gestorcolegio.controller;


import com.gestorcolegio.dto.AsistenciaRequestDto;
import com.gestorcolegio.dto.AsistenciaResponseDto;
import com.gestorcolegio.service.AsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asistencias")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AsistenciaController {
private final AsistenciaService asistenciaService;

@PostMapping
    public ResponseEntity<AsistenciaResponseDto> guardarAsistencia(@RequestBody AsistenciaRequestDto asistenciaDto){
    AsistenciaResponseDto asistencia = asistenciaService.guardarAsistencia(asistenciaDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(asistencia);
}
@GetMapping
    public ResponseEntity<List<AsistenciaResponseDto>> listarAsistencias(){
    List<AsistenciaResponseDto> asistencias = asistenciaService.listarAsistencias();
    return ResponseEntity.ok().body(asistencias);
}
@GetMapping("/clase/{claseId}")
    public ResponseEntity<List<AsistenciaResponseDto>> obtenerPorClase(@PathVariable Long claseId) {
    return ResponseEntity.ok(asistenciaService.listarPorClase(claseId));
}
@DeleteMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDto> eliminarAsistencia(@PathVariable Long id){
    asistenciaService.eliminarAsistencia(id);
    return ResponseEntity.noContent().build();
}
@PostMapping("/registrar-lista")
    public ResponseEntity<Void> guardarLista(@RequestBody List<AsistenciaRequestDto> lista) {
        asistenciaService.guardarListaAsistencia(lista);
        return ResponseEntity.status(HttpStatus.CREATED).build();
}
@PutMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDto> actualizarAsistencia(@PathVariable Long id, @RequestBody AsistenciaRequestDto asistenciaDto){
    AsistenciaResponseDto asistencia = asistenciaService.actualizarAsistencia(id, asistenciaDto);
    return ResponseEntity.ok().body(asistencia);
}

}
