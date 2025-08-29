import { BehaviorSubject, Observable } from "rxjs";
import { Injectable } from "@angular/core";
import { User } from "../interfaces/user.interface";

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private isLogged = false;
  private user: User | undefined;
  private isLoggedSubject = new BehaviorSubject<boolean>(this.isLogged);

  public $isLogged(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
  }

  public getCurrentUser(): User | undefined {
    return this.user;
  }

  public logIn(user: User): void {
    this.user = user;
    this.isLogged = true;
    this.isLoggedSubject.next(true);
  }

  public logOut(): void {
    localStorage.removeItem('token');
    this.user = undefined;
    this.isLogged = false;
    this.isLoggedSubject.next(false);
  }

  public isUserLoggedIn(): boolean {
    return this.isLogged;
  }
}
