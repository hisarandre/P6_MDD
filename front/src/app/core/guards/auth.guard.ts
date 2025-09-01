import { CanActivate, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { SessionService } from '../services/session.service';
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private sessionService: SessionService,
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
