import { BehaviorSubject, Observable } from "rxjs";
import { Injectable } from "@angular/core";
import { User } from "../interfaces/user.interface";

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private isLogged = false;
  private user: User | null = null;
  private isLoggedSubject = new BehaviorSubject<boolean>(this.isLogged);
  private initialized = false;
  private initializationSubject = new BehaviorSubject<boolean>(false);

  constructor() {
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

  public isInitialized(): boolean {
    return this.initialized;
  }

  private initializeFromStorage(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.isLogged = true;
      this.isLoggedSubject.next(true);
    }

    this.initialized = true;
    this.initializationSubject.next(true);
  }
}
