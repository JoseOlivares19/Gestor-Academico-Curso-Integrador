import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Alumno } from '../../../models/Alumno';
import { AlumnoService } from '../../../core/service/alumno-service';
import { CursoService } from '../../../core/service/curso-service'; // Importante
import { Curso } from '../../../models/Curso';

@Component({
  selector: 'app-alumnos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './alumnos.html',
  styleUrl: './alumnos.css'
})
export class AlumnosComponent implements OnInit {

  alumnos: Alumno[] = [];
  cursos: Curso[] = [];
  mostrarFormulario: boolean = false;
  alumnoTemp: any = { nombre: '', cursosIds: [] };
  idEditando: number | null = null;

  constructor(
    private alumnoService: AlumnoService,
    private cursoService: CursoService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarAlumnos();
    this.cargarCursos();
  }

  cargarAlumnos() {
    this.alumnoService.listar().subscribe({
      next: (data) => {
        this.alumnos = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error al cargar alumnos:', err)
    });
  }

  cargarCursos() {
    this.cursoService.listar().subscribe({
      next: (data) => {
        this.cursos = data;
        this.cdr.detectChanges();
      }
    });
  }

  toggleFormulario() {
    this.mostrarFormulario = !this.mostrarFormulario;
    if (!this.mostrarFormulario) {
      this.limpiarFormulario();
    }
  }

  limpiarFormulario() {
    this.alumnoTemp = { nombre: '', cursosIds: [] };
    this.idEditando = null;
  }

  onCursoChange(cursoId: number, event: any) {
    if (event.target.checked) {
      this.alumnoTemp.cursosIds.push(cursoId);
    } else {
      this.alumnoTemp.cursosIds = this.alumnoTemp.cursosIds.filter((id: number) => id !== cursoId);
    }
  }

  editarAlumno(alumno: Alumno) {
    this.alumnoTemp = {
      nombre: alumno.nombre,
      cursosIds: alumno.cursosIds ? [...alumno.cursosIds] : []
    };
    this.idEditando = alumno.id;
    this.mostrarFormulario = true;
  }

  guardarAlumno() {
    if (!this.alumnoTemp.nombre.trim()) return;

    if (this.idEditando) {
      this.alumnoService.actualizar(this.idEditando, this.alumnoTemp).subscribe({
        next: () => {
          this.cargarAlumnos();
          this.toggleFormulario();
        },
        error: (err) => console.error('Error al actualizar', err)
      });
    } else {
      this.alumnoService.registrar(this.alumnoTemp).subscribe({
        next: () => {
          this.cargarAlumnos();
          this.toggleFormulario();
        },
        error: (err) => console.error('Fallo al crear', err)
      });
    }
  }

  eliminarAlumno(id: number) {
    if (confirm('¿Estás seguro de que deseas eliminar este alumno?')) {
      this.alumnoService.eliminar(id).subscribe({
        next: () => this.cargarAlumnos(),
        error: (err) => console.error('Error al eliminar:', err)
      });
    }
  }
}
