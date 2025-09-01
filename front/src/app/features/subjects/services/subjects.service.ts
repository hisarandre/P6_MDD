import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { environment } from "../../../../environments/environment";
import { SubjectWithStatus } from "../interfaces/subjectWithStatus.interface";
import {SubjectName} from "../interfaces/subjectName.interface";
import {SubjectSubscribed} from "../interfaces/subjectSubscribed.interface";

@Injectable({ providedIn: 'root' })
export class SubjectsService {
  private pathService = environment.baseUrl + 'subjects';

  constructor(private httpClient: HttpClient) {}

  getSubjectsForSelect(): Observable<SubjectName[]> {
    return this.httpClient.get<SubjectName[]>(`${this.pathService}/names`);
  }

  getAllSubjectsWithStatus(): Observable<SubjectWithStatus[]> {
    return this.httpClient.get<SubjectWithStatus[]>(`${this.pathService}/subscriptions/status`);
  }

  getSubscribedSubjects(): Observable<SubjectWithStatus[]> {
    return this.httpClient.get<SubjectWithStatus[]>(`${this.pathService}/subscribed`);
  }

  subscribe(subjectId: number): Observable<SubjectWithStatus[] > {
    return this.httpClient.post<SubjectWithStatus[] >(`${this.pathService}/${subjectId}/subscribe`, {});
  }

  unsubscribe(subjectId: number): Observable<SubjectSubscribed[]> {
    return this.httpClient.delete<SubjectSubscribed[]>(`${this.pathService}/${subjectId}/unsubscribe`, {});
  }
}
