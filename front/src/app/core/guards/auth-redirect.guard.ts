import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import {SessionService} from "../services/session.service";
import {AuthService} from "../../features/auth/services/auth.service";

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
    // Check if user is already logged in
    if (this.sessionService.isUserLoggedIn()) {
      this.router.navigate(['/posts']);
      return false;
    }

    // Check if there is a token in localStorage
    const token = localStorage.getItem('token');
    if (!token) {
      return true;
    }

    // Attempt auto-login with existing token
    return this.authService.autoLogin(token).pipe(
      map((user) => {
        if (user) {
          this.router.navigate(['/posts']);
          return false;
        }
        return true;
      }),
      catchError((error) => {
        // Token expired or invalid, clear it
        this.sessionService.logOut();
        return of(true);
      })
    );
  }
}
