import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {
  userEmail: string = '';

  ngOnInit() {
    this.userEmail = sessionStorage.getItem('email') || '';
  }

  logout() {
    sessionStorage.clear();
    window.location.href = '/home';
  }
}
