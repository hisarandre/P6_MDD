import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { SessionService } from '../services/session.service';

@Injectable({
  providedIn: 'root'
})
export class AuthRedirectGuard implements CanActivate {
  constructor(
    private sessionService: SessionService,
    private router: Router
  ) {}

  canActivate(): Observable<boolean> | boolean {
    // Already logged in
    if (this.sessionService.isUserLoggedIn()) {
      this.router.navigate(['/posts']);
      return false;
    }

    return true;
  }
}
