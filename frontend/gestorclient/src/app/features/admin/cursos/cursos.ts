import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Curso } from '../../../models/Curso';
import { CursoService } from '../../../core/service/curso-service';
import { ProfesorService } from '../../../core/service/profesor-service';
import { ClaseService } from '../../../core/service/clase-service';

@Component({
  selector: 'app-cursos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cursos.html',
  styleUrl: './cursos.css'
})
export class CursosComponent implements OnInit {
  cursos: Curso[] = [];
  profesores: any[] = [];
  mostrarFormulario: boolean = false;
  cursoTemp: any = { nombre: '', idProfesor: null };
  idEditando: number | null = null;

  cursoSeleccionado: any = null;
  clasesDelCurso: any[] = [];
  nuevaClase: any = { tema: '', fecha: '' };

  constructor(
    private cursoService: CursoService,
    private profesorService: ProfesorService,
    private claseService: ClaseService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarCursos();
    this.cargarProfesores();
  }

  cargarCursos() {
    this.cursoService.listar().subscribe({
      next: (data) => {
        this.cursos = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error al cargar cursos:', err)
    });
  }

  cargarProfesores() {
    this.profesorService.listar().subscribe({
      next: (data) => {
        this.profesores = data;
        this.cdr.detectChanges();
      }
    });
  }

  obtenerNombreProfesor(id: number | null): string {
    if (!id) return 'Sin asignar';
    const profe = this.profesores.find(p => p.id === id);
    return profe ? profe.nombreProfesor : 'Cargando...';
  }

  toggleFormulario() {
    this.mostrarFormulario = !this.mostrarFormulario;
    if (!this.mostrarFormulario) this.limpiarFormulario();
  }

  limpiarFormulario() {
    this.cursoTemp = { nombre: '', idProfesor: null };
    this.idEditando = null;
  }

  editarCurso(curso: Curso) {
    this.cursoTemp = { nombre: curso.nombre, idProfesor: curso.idProfesor };
    this.idEditando = curso.id;
    this.mostrarFormulario = true;
  }

  guardarCurso() {
    if (!this.cursoTemp.nombre.trim()) return;

    const operacion = this.idEditando
      ? this.cursoService.actualizar(this.idEditando, this.cursoTemp)
      : this.cursoService.registrar(this.cursoTemp);

    operacion.subscribe({
      next: () => {
        this.cargarCursos();
        this.toggleFormulario();
      }
    });
  }

  eliminarCurso(id: number) {
    if (confirm('¿Deseas eliminar este curso?')) {
      this.cursoService.eliminar(id).subscribe({
        next: () => this.cargarCursos()
      });
    }
  }

  verClases(curso: any) {
    this.cursoSeleccionado = curso;
    this.claseService.listarPorCurso(curso.id).subscribe({
      next: (data) => {
        this.clasesDelCurso = data;
        this.cdr.detectChanges();
      }
    });
  }

  agregarClase() {
    if (!this.nuevaClase.tema || !this.nuevaClase.fecha) return;
    const claseData = { ...this.nuevaClase, cursoId: this.cursoSeleccionado.id };
    this.claseService.registrar(claseData).subscribe({
      next: () => {
        this.verClases(this.cursoSeleccionado);
        this.nuevaClase = { tema: '', fecha: '' };
      }
    });
  }

  borrarClase(id: number) {
    if (confirm('¿Eliminar esta clase?')) {
      this.claseService.eliminar(id).subscribe({
        next: () => this.verClases(this.cursoSeleccionado)
      });
    }
  }
}
