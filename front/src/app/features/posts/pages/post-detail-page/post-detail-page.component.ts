import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import {Observable, Subject, takeUntil, tap} from "rxjs";
import { AsyncPipe } from "@angular/common";
import { SubHeaderComponent } from "../../../../layout/components/sub-header/sub-header.component";
import { PostDetailsCardComponent } from "../../components/post-details-card/post-details-card.component";
import { PostsService } from "../../services/posts.service";
import { Post } from "../../interfaces/post.interface";
import {PostDetails} from "../../interfaces/postDetails.interface";
import {CommentsListComponent} from "../../../comments/components/comments-list/comments-list.component"; // Adjust the import path as needed

@Component({
  selector: 'app-post-detail-page',
  standalone: true,
  imports: [
    SubHeaderComponent,
    PostDetailsCardComponent,
    AsyncPipe,
    CommentsListComponent
  ],
  templateUrl: './post-detail-page.component.html',
  styleUrl: './post-detail-page.component.scss'
})
export class PostDetailPageComponent implements OnInit, OnDestroy {
  public post$!: Observable<PostDetails>;
  private destroy$ = new Subject<void>();
  public postTitle = 'Chargement...';
  public postId!: number;

  constructor(
    public readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly postsService: PostsService
  ) {}


  ngOnInit(): void {
    const postId = this.route.snapshot.paramMap.get('id');

    if (postId && !isNaN(Number(postId))) {
      this.postId = Number(postId)
      this.post$ = this.postsService.getPostById(Number(postId))
        .pipe(
          tap(post => this.postTitle = post?.title || 'Détails du post'),
          takeUntil(this.destroy$)
        );
    } else {
      this.goBack();
    }
  }

  public goBack(): void {
    this.router.navigate(['/posts']).catch(console.error);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }


}
