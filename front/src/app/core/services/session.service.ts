import {BehaviorSubject, catchError, Observable, of, tap} from "rxjs";
import { Injectable } from "@angular/core";
import { User } from "../interfaces/user.interface";
import {AuthService} from "../../features/auth/services/auth.service";

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private isLogged = false;
  private user: User | null = null;
  private isLoggedSubject = new BehaviorSubject<boolean>(this.isLogged);
  private initializationSubject = new BehaviorSubject<boolean>(false);

  constructor(
    private authService: AuthService
  ) {
    this.initializeFromStorage();
  }

  public $isLogged(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
  }

  public getCurrentUser(): User | null {
    return this.user;
  }

  public logIn(user: User): void {
    this.user = user;
    this.isLogged = true;
    this.isLoggedSubject.next(true);
  }

  public logOut(): void {
    localStorage.removeItem('token');
    this.user = null;
    this.isLogged = false;
    this.isLoggedSubject.next(false);
  }

  public isUserLoggedIn(): boolean {
    return this.isLogged;
  }

  public autoLogin(token: string): Observable<User> {
    localStorage.setItem('token', token);

    return this.authService.getUserProfile().pipe(
      tap(user => this.logIn(user))
    );
  }

  private initializeFromStorage(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.isLogged = true;
      this.isLoggedSubject.next(true);

      this.autoLogin(token).pipe(
        catchError(err => {
          this.logOut();
          return of(null);
        })
      ).subscribe();
    }

    this.initializationSubject.next(true);
  }

}
