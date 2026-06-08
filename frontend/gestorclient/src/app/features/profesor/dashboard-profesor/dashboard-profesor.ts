import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CursoService } from '../../../core/service/curso-service';
import { ClaseService } from '../../../core/service/clase-service';
import { AlumnoService } from '../../../core/service/alumno-service';
import { AsistenciaService } from '../../../core/service/asistencia.service';
import { Curso } from '../../../models/Curso';

@Component({
  selector: 'app-dashboard-profesor',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-profesor.html',
  styleUrl: './dashboard-profesor.css',
})
export class DashboardProfesor implements OnInit {
  misCursos: Curso[] = [];
  cursoSeleccionado: any = null;
  clasesDelCurso: any[] = [];
  alumnosDelCurso: any[] = [];
  claseSeleccionada: any = null;
  mostrarAlumnos: boolean = false;
  asistenciaGuardada: boolean = false;

  constructor(
    private cursoService: CursoService,
    private claseService: ClaseService,
    private alumnoService: AlumnoService,
    private asistenciaService: AsistenciaService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.cargarMisCursos();
  }

  cargarMisCursos() {
    this.cursoService.listarMisCursos().subscribe({
      next: (data) => {
        this.misCursos = data;
        this.cdr.detectChanges();
      }
    });
  }

  verAgenda(curso: any) {
    this.mostrarAlumnos = false;
    this.cursoSeleccionado = curso;
    this.claseSeleccionada = null;
    this.asistenciaGuardada = false;
    this.claseService.listarPorCurso(curso.id).subscribe({
      next: (data) => {
        this.clasesDelCurso = data;
        this.cdr.detectChanges();
      }
    });
  }

  seleccionarClase(clase: any) {
    this.claseSeleccionada = clase;
    this.asistenciaGuardada = false;

    this.alumnoService.listarPorCurso(this.cursoSeleccionado.id).subscribe({
      next: (alumnos) => {

        this.asistenciaService.obtenerPorClase(clase.id).subscribe({
          next: (asistenciasGuardadas) => {

            this.alumnosDelCurso = alumnos.map((alumno: any) => {
              const registro = asistenciasGuardadas.find((a: any) =>
                a.alumnoId === alumno.id || (a.alumno && a.alumno.id === alumno.id)
              );

              return {
                ...alumno,
                estadoAsistencia: registro ? String(registro.estado || registro.estadoAsistencia).toUpperCase() : 'PRESENTE'
              };
            });

            if (asistenciasGuardadas.length > 0) {
              this.asistenciaGuardada = true;
            }

            this.mostrarAlumnos = true;
            this.cdr.detectChanges();
          },
          error: () => {
            this.alumnosDelCurso = alumnos.map((a: any) => ({ ...a, estadoAsistencia: 'PRESENTE' }));
            this.mostrarAlumnos = true;
            this.cdr.detectChanges();
          }
        });
      }
    });
  }

  guardarAsistenciaGeneral() {
    if (!this.claseSeleccionada) {
      alert("Por favor, selecciona una clase de la agenda primero.");
      return;
    }

    const listaAsistencia = this.alumnosDelCurso.map(alumno => ({
      alumnoId: alumno.id,
      claseId: this.claseSeleccionada.id,
      estado: alumno.estadoAsistencia
    }));

    this.asistenciaService.registrarLista(listaAsistencia).subscribe({
      next: () => {
        this.asistenciaGuardada = true;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al guardar la lista:', err);
        alert('Hubo un error al guardar la asistencia.');
      }
    });
  }
}
