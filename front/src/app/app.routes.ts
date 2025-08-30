import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'home-page',
    pathMatch: 'full'
  },
  {
    path: 'home-page',
    loadComponent: () => import('./features/home/pages/home-page/home-page.component').then(c => c.HomePageComponent),
  },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/pages/login-page/login-page.component').then(c => c.LoginPageComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/pages/register-page/register-page.component').then(c => c.RegisterPageComponent)
  },
  {
    path: 'me',
    loadComponent: () => import('./features/me/pages/me-page/me-page.component').then(c => c.MePageComponent)
  },
  {
    path: 'posts',
    loadChildren: () => import('./features/posts/posts.routes').then(r => r.POSTS_ROUTES)
  },
  {
    path: 'subjects',
    loadComponent: () => import('./features/subjects/pages/subjects-page/subjects-page.component').then(c => c.SubjectsPageComponent)
  }
];
