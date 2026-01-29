import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    loadComponent: () => import('./home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'admin/dashboard',
    loadComponent: () => import('./dashboard/admin-dashboard.component').then(m => m.AdminDashboardComponent)
  },
  {
    path: 'customer/dashboard',
    loadComponent: () => import('./dashboard/customer-dashboard.component').then(m => m.CustomerDashboardComponent)
  },
  {
    path: 'bills',
    loadComponent: () => import('./view-bills/view-bills.component').then(m => m.ViewBillsComponent),
    canActivate: [authGuard]
  },
  {
    path: 'pay-bill',
    loadComponent: () => import('./pay-bill/pay-bill.component').then(m => m.PayBillComponent),
    canActivate: [authGuard]
  },
  {
    path: 'create-bill',
    loadComponent: () => import('./create-bill/create-bill.component').then(m => m.CreateBillComponent)
  }
];

