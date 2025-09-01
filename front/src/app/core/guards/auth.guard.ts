import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
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
    if (this.sessionService.isUserLoggedIn()) {
      return true;
    }

    this.router.navigate(['/home']);
    return false;
  }
}
