import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { CommentCardComponent } from '../comment-card/comment-card.component';
import { Comment } from '../../interfaces/comment.interface';
import { CommentsService } from '../../services/comments.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIcon } from '@angular/material/icon';
import { MatIconButton } from '@angular/material/button';
import { MatFormField, MatInput } from '@angular/material/input';
import { CdkTextareaAutosize } from '@angular/cdk/text-field';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subject, BehaviorSubject, takeUntil, catchError, of } from 'rxjs';
import { AddCommentRequest } from '../../interfaces/addCommentRequest.interface';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-comments-list',
  imports: [
    AsyncPipe,
    CommentCardComponent,
    ReactiveFormsModule,
    MatIcon,
    MatIconButton,
    MatFormField,
    MatInput,
    CdkTextareaAutosize
  ],
  templateUrl: './comments-list.component.html',
  styleUrls: ['./comments-list.component.scss']
})
export class CommentsListComponent implements OnInit, OnDestroy {

  @Input() postId?: number;

  commentForm!: FormGroup;

  private commentsSubject = new BehaviorSubject<Comment[]>([]);
  allComments$ = this.commentsSubject.asObservable();

  private destroy$ = new Subject<void>();

  constructor(
    private readonly commentsService: CommentsService,
    private readonly fb: FormBuilder,
    private readonly snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.loadComments();
  }

  private initForm(): void {
    this.commentForm = this.fb.group({
      content: ['']
    });
  }

  private loadComments(): void {
    if (!this.postId) return;

    this.commentsService.getCommentsByPostId(this.postId)
      .pipe(
        catchError(() => of([])),
        takeUntil(this.destroy$)
      )
      .subscribe((comments: Comment[]) => {
        this.commentsSubject.next(comments);
      });
  }

  public get content() {
    return this.commentForm.get('content');
  }

  onSubmitComment(): void {
    const commentContent = this.content?.value.trim();
    if (!commentContent || !this.postId) return;

    const addCommentRequest: AddCommentRequest = { content: commentContent };

    this.commentsService.addComment(this.postId, addCommentRequest)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.handleSubmissionError(error);
          return of([] as Comment[]);
        }),
        takeUntil(this.destroy$)
      )
      .subscribe((updatedComments: Comment[]) => {
        this.commentsSubject.next(updatedComments);
        this.commentForm.reset();
      });
  }

  private handleSubmissionError(error: HttpErrorResponse): void {
    this.snackBar.open('Erreur lors de l\'ajout du commentaire', 'Fermer', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom'
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.commentsSubject.complete();
  }
}
