import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { SessionService } from '../services/session.service';
import { AuthService } from '../../features/auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private sessionService: SessionService,
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): Observable<boolean> | boolean {
    // Already logged in
    if (this.sessionService.isUserLoggedIn()) {
      return true;
    }

    // Check token in localStorage
    const token = localStorage.getItem('token');
    if (!token) {
      //No token found, redirecting to /home
      this.router.navigate(['/home']);
      return false;
    }

    // Attempt auto-login
    return this.authService.autoLogin(token).pipe(
      map(user => {
        if (user) {
          return true;
        }
        // Token invalid or expired, redirecting to /home
        this.sessionService.logOut();
        this.router.navigate(['/home']);
        return false;
      }),
      catchError(err => {
        this.sessionService.logOut();
        this.router.navigate(['/home']);
        return of(false);
      })
    );
  }
}
