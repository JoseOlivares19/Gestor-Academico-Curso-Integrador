import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Sidebar } from '../../../shared/components/layout/sidebar/sidebar';
import { Header } from '../../../shared/components/layout/header/header';

@Component({
  selector: 'app-profesor-layout',
  imports: [RouterOutlet, Sidebar, Header],
  templateUrl: './profesor-layout.html',
  styleUrl: './profesor-layout.css'
})
export class ProfesorLayout { }
