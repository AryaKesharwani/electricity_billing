import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  features = [
    {
      icon: '👤',
      title: 'Customer Registration',
      description: 'Register your account to manage your electricity bills',
      route: '/register',
      color: '#667eea'
    },
    {
      icon: '🔐',
      title: 'User Login',
      description: 'Sign in to access your account and view bills',
      route: '/login',
      color: '#764ba2'
    },
    {
      icon: '📄',
      title: 'View Bills',
      description: 'Check all your electricity bills and payment history',
      route: '/bills',
      color: '#f093fb'
    },
    {
      icon: '💳',
      title: 'Pay Bills',
      description: 'Pay your pending bills online securely',
      route: '/pay-bill',
      color: '#4facfe'
    },
    {
      icon: '👨‍💼',
      title: 'Admin Portal',
      description: 'Administrator registration and management',
      route: '/admin/register',
      color: '#f5576c'
    }
  ];
}
