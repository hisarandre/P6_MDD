import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {map, Observable, switchMap, tap} from 'rxjs';
import { RegisterRequest } from "../interfaces/registerRequest.interface";
import { LoginRequest } from "../interfaces/loginRequest.interface";
import { environment } from "../../../../environments/environment";
import { AuthToken } from "../interfaces/authToken.interface";
import {User} from "../../../core/interfaces/user.interface";
import {SessionService} from "../../../core/services/session.service";


@Injectable({ providedIn: 'root' })
export class AuthService {
  private pathService = environment.baseUrl + 'auth';

  constructor(
    private httpClient: HttpClient,
    private sessionService: SessionService
  ) { }

  public register(registerRequest: RegisterRequest): Observable<string> {
    return this.httpClient.post<AuthToken>(`${this.pathService}/register`, registerRequest).pipe(
      map(response => response.token)
    );
  }

  public login(loginRequest: LoginRequest): Observable<User> {
    return this.httpClient.post<AuthToken>(`${this.pathService}/login`, loginRequest).pipe(
      map(response => response.token),
      tap(token => localStorage.setItem('token', token)),
      switchMap(token => this.getUserProfile()),
      tap(user => this.sessionService.logIn(user))
    );
  }

  public performAutoLogin(token: string): Observable<User> {
    localStorage.setItem('token', token);

    return this.getUserProfile().pipe(
      tap(user => this.sessionService.logIn(user))
    );
  }

  private getUserProfile(): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/me`);
  }
}
