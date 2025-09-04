import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { SessionService } from '../services/session.service';
import { AuthService } from '../../features/auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthRedirectGuard implements CanActivate {
  constructor(
    private sessionService: SessionService,
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): Observable<boolean> | boolean {
    // Already logged in
    if (this.sessionService.isUserLoggedIn()) {
      this.router.navigate(['/posts']);
      return false;
    }

    // Check token in localStorage
    const token = localStorage.getItem('token');
    if (!token) {
      return true; // user not logged in so allow access
    }

    // Attempt auto-login
    return this.authService.autoLogin(token).pipe(
      map(user => {
        if (user) {
          this.router.navigate(['/posts']);
          return false;
        }
        return true;
      }),
      catchError(err => {
        this.sessionService.logOut();
        return of(true); // allow access to login/home
      })
    );
  }
}
