import {Component, Input, OnInit, OnDestroy} from '@angular/core';
import {AsyncPipe} from "@angular/common";
import {PostCardComponent} from "../../../posts/components/post-card/post-card.component";
import {Observable, of, Subject, switchMap, takeUntil, catchError, finalize} from "rxjs";
import {CommentsService} from "../../services/comments.service";
import {CommentCardComponent} from "../comment-card/comment-card.component";
import {Comment} from "../../interfaces/comment.interface";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {MatError, MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {CdkTextareaAutosize} from "@angular/cdk/text-field";
import {HttpErrorResponse} from "@angular/common/http";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AddCommentRequest} from "../../interfaces/addCommentRequest.interface";

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
  styleUrl: './comments-list.component.scss'
})
export class CommentsListComponent implements OnInit, OnDestroy {
  allComments$: Observable<Comment[]> = of([]);
  private destroy$ = new Subject<void>();
  commentForm!: FormGroup;
  isSubmitting = false;
  hasSubmissionFailed = false;

  @Input() postId: number | undefined;

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
    if (this.postId) {
      this.allComments$ = this.commentsService.getCommentsByPostId(this.postId);
    }
  }

  public get content() {
    return this.commentForm.get('content');
  }

  public get isLoading(): boolean {
    return this.isSubmitting;
  }

  onSubmitComment(): void {

    const commentContent = this.commentForm.get('content')?.value.trim();

    // Validation manuelle
    if (!commentContent || this.isSubmitting || !this.postId) return;

    this.resetState();
    this.isSubmitting = true;

    const addCommentRequest: AddCommentRequest = {
      content: commentContent
    };

    this.commentsService.addComment(this.postId, addCommentRequest)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.handleSubmissionError(error);
          return of(null);
        }),
        takeUntil(this.destroy$),
        finalize(() => this.isSubmitting = false)
      )
      .subscribe({
        next: () => {
          this.commentForm.reset();
          this.loadComments();
          this.snackBar.open('Commentaire ajouté avec succès', 'Fermer', {
            duration: 3000,
            horizontalPosition: 'center',
            verticalPosition: 'bottom'
          });
        }
      });
  }

  private resetState(): void {
    this.hasSubmissionFailed = false;
  }

  private handleSubmissionError(error: HttpErrorResponse): void {
    this.hasSubmissionFailed = true;

    this.commentForm.get('content')?.markAsUntouched();

    this.snackBar.open('Erreur lors de l\'ajout du commentaire', 'Fermer', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom'
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
