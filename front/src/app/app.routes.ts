import { Routes } from '@angular/router';
import {AuthRedirectGuard} from "./core/guards/auth-redirect.guard";
import {AuthGuard} from "./core/guards/auth.guard";

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    loadComponent: () => import('./features/auth/pages/home-page/home-page.component').then(c => c.HomePageComponent),
    canActivate: [AuthRedirectGuard]
  },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/pages/login-page/login-page.component').then(c => c.LoginPageComponent),
    canActivate: [AuthRedirectGuard]
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/pages/register-page/register-page.component').then(c => c.RegisterPageComponent),
    canActivate: [AuthRedirectGuard]
  },

  // Protected pages - require authentication

  {
    path: 'me',
    loadComponent: () => import('./features/me/pages/me-page/me-page.component').then(c => c.MePageComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'posts',
    loadChildren: () => import('./features/posts/posts.routes').then(r => r.POSTS_ROUTES),
    canActivate: [AuthGuard]
  },
  {
    path: 'subjects',
    loadComponent: () => import('./features/subjects/pages/subjects-page/subjects-page.component').then(c => c.SubjectsPageComponent),
    canActivate: [AuthGuard]
  }
];
