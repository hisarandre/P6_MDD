import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { AsyncPipe } from "@angular/common";
import { Observable, of, Subject } from "rxjs";
import { Post } from "../../interfaces/post.interface";
import { PostsService } from "../../services/posts.service";
import { PostCardComponent } from "../post-card/post-card.component";

@Component({
  selector: 'app-posts-list',
  imports: [
    AsyncPipe,
    PostCardComponent
  ],
  templateUrl: './posts-list.component.html',
  styleUrl: './posts-list.component.scss'
})
export class PostsListComponent implements OnInit, OnDestroy {
  private _sortAscending = true;

  @Input()
  set sortAscending(value: boolean) {
    this._sortAscending = value;
    this.loadPosts();
  }

  get sortAscending(): boolean {
    return this._sortAscending;
  }

  allPosts$: Observable<Post[]> = of();
  private destroy$ = new Subject<void>();

  constructor(
    private readonly postsService: PostsService,
  ) { }

  ngOnInit(): void {
    this.loadPosts();
  }

  private loadPosts(): void {
    this.allPosts$ = this.postsService.getSubscribedPosts(this.sortAscending);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
