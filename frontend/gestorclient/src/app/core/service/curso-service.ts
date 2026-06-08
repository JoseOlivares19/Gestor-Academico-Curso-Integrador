import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Curso } from '../../models/Curso';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class CursoService {
  private apiUrl = 'http://localhost:8080/api/v1/cursos';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders() {
    const token = this.authService.getToken();
    return {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`
      })
    };
  }

  listar(): Observable<Curso[]> {
    return this.http.get<Curso[]>(this.apiUrl, this.getHeaders());
  }

  listarMisCursos(): Observable<Curso[]> {
  return this.http.get<Curso[]>(`${this.apiUrl}/mis-cursos`, this.getHeaders());
  }

  registrar(curso: any): Observable<any> {
    return this.http.post(this.apiUrl, curso, this.getHeaders());
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, this.getHeaders());
  }

  actualizar(id: number, curso: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, curso, this.getHeaders());
  }
}
