import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { environment } from "../../../../environments/environment";
import {AddCommentRequest} from "../interfaces/addCommentRequest.interface";
import {Comment} from "../interfaces/comment.interface";


@Injectable({ providedIn: 'root' })
export class CommentsService {
  private pathService = environment.baseUrl + 'comments';

  constructor(private httpClient: HttpClient) {}

  getCommentsByPostId(postId: number): Observable<Comment[]> {
    return this.httpClient.get<Comment[]>(`${this.pathService}/post/${postId}`);
  }

  addComment(postId: number, createRequest: AddCommentRequest): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/post/${postId}`, createRequest);
  }
}
