import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AsistenciaService {
  private apiUrl = 'http://localhost:8080/api/v1/asistencias';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders() {
    const token = this.authService.getToken();
    return {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`
      })
    };
  }

  listarAsistencias(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, this.getHeaders());
  }

  guardarAsistencia(asistencia: any): Observable<any> {
    return this.http.post(this.apiUrl, asistencia, this.getHeaders());
  }

  registrarLista(lista: any[]): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/registrar-lista`, lista, this.getHeaders());
  }

  eliminarAsistencia(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, this.getHeaders());
  }
  actualizarAsistencia(id: number, asistencia: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, asistencia, this.getHeaders());
  }
  obtenerPorClase(claseId: number): Observable<any[]> {
  return this.http.get<any[]>(`${this.apiUrl}/clase/${claseId}`, this.getHeaders());
  }
}
