import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    loadComponent: () => import('./features/home/pages/home/home.component').then(c => c.HomeComponent),
  },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/pages/login/login.component').then(c => c.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/pages/register/register.component').then(c => c.RegisterComponent)
  },
  {
    path: 'me',
    loadComponent: () => import('./features/me/pages/me/me.component').then(c => c.MeComponent)
  },
  {
    path: 'posts',
    loadComponent: () => import('./features/me/pages/me/me.component').then(c => c.MeComponent)
  },
  {
    path: 'subjects',
    loadComponent: () => import('./features/me/pages/me/me.component').then(c => c.MeComponent)
  }
];
