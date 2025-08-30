import { Routes } from '@angular/router';

export const POSTS_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('../posts/pages/posts-feed-page/posts-feed-page.component').then(c => c.PostsFeedPageComponent)
  },
  {
    path: 'create',
    loadComponent: () => import('../posts/pages/create-post-page/create-post-page.component').then(c => c.CreatePostPageComponent)
  },
  {
    path: ':id',
    loadComponent: () => import('../posts/pages/post-detail-page/post-detail-page.component').then(c => c.PostDetailPageComponent)
  }
];
