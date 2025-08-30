import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { environment } from "../../../../environments/environment";
import { SubjectWithStatus } from "../interfaces/subjectWithStatus.interface";

@Injectable({ providedIn: 'root' })
export class SubjectsService {
  private pathService = environment.baseUrl + 'subjects';

  constructor(private httpClient: HttpClient) {}

  getAllSubjects(): Observable<SubjectWithStatus[]> {
    return this.httpClient.get<SubjectWithStatus[]>(`${this.pathService}/subscriptions/status`);
  }

  getSubscribedSubjects(): Observable<SubjectWithStatus[]> {
    return this.httpClient.get<SubjectWithStatus[]>(`${this.pathService}/subscribed`);
  }

  subscribe(subjectId: number): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/${subjectId}/subscribe`, {});
  }

  unsubscribe(subjectId: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.pathService}/${subjectId}/unsubscribe`, {});
  }
}
