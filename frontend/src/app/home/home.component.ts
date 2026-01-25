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
    },
    {
      icon: '➕',
      title: 'Create Bill',
      description: 'Generate new bills for customers (Admin only)',
      route: '/create-bill',
      color: '#43e97b'
    }
  ];

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
