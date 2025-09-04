
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from "../../../../environments/environment";
import { User } from "../../../core/interfaces/user.interface";
import { UpdateUserRequest } from "../interfaces/UpdateUserRequest.interface";


@Injectable({ providedIn: 'root' })
export class UsersService {
  private pathService = environment.baseUrl + 'users';

  constructor(
    private httpClient: HttpClient,
  ) { }

  updateProfile(updateRequest: UpdateUserRequest): Observable<User> {
    return this.httpClient.put<User>(`${this.pathService}/me`, updateRequest);
  }
}
