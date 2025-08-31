import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { environment } from "../../../../environments/environment";
import {Post} from "../interfaces/post.interface";
import {PostDetails} from "../interfaces/post-details.interface";

@Injectable({ providedIn: 'root' })
export class PostsService {
  private pathService = environment.baseUrl + 'posts';

  constructor(private httpClient: HttpClient) {}

  getSubscribedPosts(sortAscending: boolean): Observable<Post[]> {
    const sort = sortAscending ? 'asc' : 'desc'
    return this.httpClient.get<Post[]>(`${this.pathService}/subscribed?sort=${sort}`);
  }

  getPostDetails(postId: number): Observable<PostDetails> {
    return this.httpClient.get<PostDetails>(`${this.pathService}/${postId}` );
  }

}
