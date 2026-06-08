import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProfesorService } from '../../../core/service/profesor-service';
import { UsuarioService } from '../../../core/service/usuario-service';

@Component({
  selector: 'app-profesores',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profesores.html'
})
export class ProfesoresComponent implements OnInit {
  profesores: any[] = [];
  usuarios: any[] = [];
  mostrarFormulario: boolean = false;
  idEditando: number | null = null;

  profesorTemp: any = { nombreProfesor: '', habilitado: true, usuarioId: null };

  constructor(
    private profesorService: ProfesorService,
    private usuarioService: UsuarioService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarProfesores();
    this.cargarUsuarios();
  }

  cargarProfesores() {
    this.profesorService.listar().subscribe({
      next: (data) => {
        this.profesores = data;
        this.cdr.detectChanges();
      }
    });
  }

  cargarUsuarios() {
    this.usuarioService.listarTodos().subscribe({
      next: (data) => {
        this.usuarios = data.filter((u: any) => u.rol === 'PROFESOR');
        this.cdr.detectChanges();
      }
    });
  }

  toggleFormulario() {
    this.mostrarFormulario = !this.mostrarFormulario;
    if (!this.mostrarFormulario) this.limpiarFormulario();
  }

  limpiarFormulario() {
    this.profesorTemp = { nombreProfesor: '', habilitado: true, usuarioId: null };
    this.idEditando = null;
  }

  editarProfesor(profe: any) {
    const userAsignado = this.usuarios.find(u => u.username === profe.username);

    this.profesorTemp = {
      nombreProfesor: profe.nombreProfesor,
      habilitado: profe.habilitado,
      usuarioId: userAsignado ? userAsignado.id : null
    };
    this.idEditando = profe.id;
    this.mostrarFormulario = true;
  }

  guardarProfesor() {
    if (!this.profesorTemp.nombreProfesor.trim()) return;

    if (this.idEditando) {
      this.profesorService.actualizar(this.idEditando, this.profesorTemp).subscribe({
        next: () => {
          this.cargarProfesores();
          this.toggleFormulario();
        }
      });
    } else {
      this.profesorService.registrar(this.profesorTemp).subscribe({
        next: () => {
          this.cargarProfesores();
          this.toggleFormulario();
        }
      });
    }
  }

  eliminarProfesor(id: number) {
    if (confirm('¿Deseas eliminar este profesor?')) {
      this.profesorService.eliminar(id).subscribe({
        next: () => this.cargarProfesores()
      });
    }
  }
}
