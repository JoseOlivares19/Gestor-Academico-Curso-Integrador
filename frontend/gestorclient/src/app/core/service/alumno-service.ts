import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Alumno } from '../../models/Alumno';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class AlumnoService {
  private apiUrl = 'http://localhost:8080/api/v1/alumnos';

  constructor(private http: HttpClient, private authService: AuthService) {}
  private getHeaders() {
    const token = this.authService.getToken();
    return {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`
      })
    };
  }

  registrar(alumno: any): Observable<any> {
    return this.http.post(this.apiUrl, alumno, this.getHeaders());
  }

  listar(): Observable<Alumno[]> {
    return this.http.get<Alumno[]>(this.apiUrl, this.getHeaders());
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, this.getHeaders());
  }

  listarPorCurso(cursoId: number): Observable<any[]> {
  return this.http.get<any[]>(`${this.apiUrl}/curso/${cursoId}`, this.getHeaders());
  }

  actualizar(id: number, alumno: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, alumno, this.getHeaders());
  }
}
