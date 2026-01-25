import { Routes } from '@angular/router';

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
    path: 'admin/register',
    loadComponent: () => import('./admin-register/admin-register.component').then(m => m.AdminRegisterComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'admin/dashboard',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'customer/dashboard',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'bills',
    loadComponent: () => import('./view-bills/view-bills.component').then(m => m.ViewBillsComponent)
  }
];

