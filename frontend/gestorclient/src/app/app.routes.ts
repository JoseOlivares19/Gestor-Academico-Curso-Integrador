import { Routes } from '@angular/router';
import { RegisterComponent } from './auth/register/register.component';
import { LoginComponent } from './auth/login/login.component';
import { Dashboard } from './features/admin/dashboard/dashboard';
import { DashboardProfesor } from './features/profesor/dashboard-profesor/dashboard-profesor';
import { AdminLayout } from './features/admin/admin-layout/admin-layout';
import { ProfesorLayout } from './features/profesor/profesor-layout/profesor-layout';
import { AlumnosComponent } from './features/admin/alumnos/alumnos';
import { CursosComponent } from './features/admin/cursos/cursos';
import { ProfesoresComponent } from './features/admin/profesores/profesores';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'admin',
    component: AdminLayout,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: Dashboard },
      { path: 'alumnos', component: AlumnosComponent },
      { path: 'cursos', component: CursosComponent },
      { path: 'profesores', component: ProfesoresComponent }
    ]
  },
  {
    path: 'profesor',
    component: ProfesorLayout,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardProfesor }
    ]
  }
];
