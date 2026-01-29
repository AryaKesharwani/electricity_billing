import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  isLoggedIn = false;
  userEmail = '';
  userType = '';

  benefits = [
    {
      icon: '🔒',
      title: 'Secure Payments',
      description: 'Bank-level encryption for all transactions'
    },
    {
      icon: '⚡',
      title: 'Instant Updates',
      description: 'Real-time bill tracking and notifications'
    },
    {
      icon: '📱',
      title: 'Easy Access',
      description: 'Manage bills from anywhere, anytime'
    },
    {
      icon: '💬',
      title: '24/7 Support',
      description: 'Round-the-clock customer assistance'
    }
  ];

  ngOnInit() {
    // Check if user is logged in
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (token) {
      this.isLoggedIn = true;
      this.userEmail = sessionStorage.getItem('email') || '';
      this.userType = sessionStorage.getItem('userType') || '';
    }
  }

  logout() {
    localStorage.removeItem('token');
    sessionStorage.clear();
    this.isLoggedIn = false;
    this.userEmail = '';
    this.userType = '';
    window.location.reload();
  }

  goToDashboard() {
    if (this.userType === 'ADMIN') {
      window.location.href = '/admin/dashboard';
    } else {
      window.location.href = '/customer/dashboard';
    }
  }
}
