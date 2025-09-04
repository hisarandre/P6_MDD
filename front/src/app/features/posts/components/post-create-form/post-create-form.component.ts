import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {catchError, finalize, Observable, of, Subject, takeUntil} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {PostsService} from "../../services/posts.service";
import {SubjectsService} from "../../../subjects/services/subjects.service";
import {SubjectName} from "../../../subjects/interfaces/subjectName.interface";
import {CreatePostRequest} from "../../interfaces/createPostRequest.interface";
import {AsyncPipe} from "@angular/common";
import {MatOption} from "@angular/material/core";
import {MatSelect} from "@angular/material/select";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-post-create-form',
  imports: [
    FormsModule,
    MatButton,
    MatFormField,
    MatInput,
    ReactiveFormsModule,
    MatOption,
    MatSelect,
    AsyncPipe,
    MatError,
  ],
  templateUrl: './post-create-form.component.html',
  styleUrl: './post-create-form.component.scss'
})
export class PostCreateFormComponent implements OnInit, OnDestroy {
  postCreateForm!: FormGroup;
  subjects$: Observable<SubjectName[]> = of([]);
  isCreating = false;
  hasCreationFailed = false;

  private destroy$ = new Subject<void>();

  constructor(
    public readonly router: Router,
    private readonly formBuilder: FormBuilder,
    private readonly postsService: PostsService,
    private readonly subjectsService: SubjectsService,
    private readonly snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.postCreateForm = this.formBuilder.group({
      subjectId: ['', [Validators.required]],
      title: ['', [Validators.required, Validators.maxLength(100)]],
      content: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(2000)]],
    });

    this.subjects$ = this.subjectsService.getSubjectsForSelect();
  }

  public get content() { return this.postCreateForm.get('content'); }

  public get isLoading(): boolean {
    return this.isCreating;
  }

  public get loadingMessage(): string {
    return this.isCreating ? 'Création en cours...' : 'Créer';
  }

  public onCreate(): void {
    if (this.postCreateForm.invalid || this.isLoading) return;

    this.resetState();
    this.isCreating = true;

    const createRequest: CreatePostRequest = this.postCreateForm.value;

    this.postsService.createPost(createRequest)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.handleCreationError(error);
          return of(null);
        }),
        takeUntil(this.destroy$),
        finalize(() => this.isCreating = false)
      )
      .subscribe({
        next: () => {
          this.snackBar.open('Post créé', 'Fermer', {
            duration: 3000,
            horizontalPosition: 'center',
            verticalPosition: 'bottom'
          });
          this.router.navigate(['/posts']).catch(console.error);
        }
      });
  }

  private resetState(): void {
    this.hasCreationFailed = false;
    this.postCreateForm.setErrors(null);
    this.isCreating = false;
  }

  private handleCreationError(error: HttpErrorResponse): void {
    this.hasCreationFailed = true;
    if (error.status === 400) {
      this.snackBar.open('Les données saisies sont invalides.', 'Fermer', {
        duration: 4000,
        horizontalPosition: 'center',
        verticalPosition: 'bottom'
      });
    } else {
      this.snackBar.open('Erreur lors de la création du post.', 'Fermer', {
        duration: 4000,
        horizontalPosition: 'center',
        verticalPosition: 'bottom'
      });
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
