import { Component } from '@angular/core';
import { AuthService } from '../../../../core/service/auth.service';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header {

  constructor(private authService: AuthService) {}

  onLogout(): void {
    this.authService.logout();
  }
}
