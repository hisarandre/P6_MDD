import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  AbstractControl,
  ValidationErrors,
  ReactiveFormsModule
} from '@angular/forms';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { Subject, finalize, takeUntil, catchError, of } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { UsersService } from '../../services/users.service';
import { MatIconModule } from '@angular/material/icon';
import { NgIf } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { SessionService } from "../../../../core/services/session.service";
import { UpdateUserRequest } from "../../interfaces/UpdateUserRequest.interface";
import { User } from "../../../../core/interfaces/user.interface";
import {passwordValidator} from "../../../../shared/validators/password.validators";

@Component({
  selector: 'app-update-me-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
  ],
  templateUrl: './update-me-form.component.html',
  styleUrl: './update-me-form.component.scss'
})
export class UpdateMeFormComponent implements OnInit, OnDestroy {
  public meForm!: FormGroup;
  public currentUser: User | null = null;

  public isLoading = false;
  public hasUpdateFailed = false;

  private destroy$ = new Subject<void>();

  constructor(
    public readonly router: Router,
    private readonly formBuilder: FormBuilder,
    private readonly userService: UsersService,
    private readonly sessionService: SessionService,
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    this.loadCurrentUser();
  }

  private initializeForm(): void {
    this.meForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
      password: ['', [Validators.minLength(8), Validators.maxLength(100), passwordValidator]],
    });
  }

  private loadCurrentUser(): void {
    const user = this.sessionService.getCurrentUser();
    if (user) {
      this.currentUser = user;
      this.populateForm(user);
    }
  }

  private populateForm(user: User): void {
    this.meForm.patchValue({
      username: user.username,
      email: user.email,
      password: ''
    });
  }

  public get username() { return this.meForm.get('username'); }
  public get email() { return this.meForm.get('email'); }
  public get password() { return this.meForm.get('password'); }

  public get loadingMessage(): string {
    return this.isLoading ? 'Mise à jour...' : 'Sauvegarder';
  }

  public goBack(): void {
    if (!this.isLoading) {
      this.router.navigate(['/posts']).catch(console.error);
    }
  }

  public onUpdate(): void {
    if (this.meForm.invalid || this.isLoading) return;

    this.resetState();
    this.isLoading = true;

    const updateRequest: UpdateUserRequest = {
      username: this.meForm.get('username')?.value,
      email: this.meForm.get('email')?.value,
      ...(this.meForm.get('password')?.value && {
        password: this.meForm.get('password')?.value
      })
    };

    this.userService.updateProfile(updateRequest)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.handleUpdateError(error);
          return of(null);
        }),
        takeUntil(this.destroy$),
        finalize(() => {
          this.isLoading = false;
        })
      )
      .subscribe({
        next: (updatedUser) => {
          if (updatedUser) {
            this.currentUser = updatedUser;
            this.sessionService.logIn(updatedUser);
          }
        }
      });
  }

  private resetState(): void {
    this.hasUpdateFailed = false;
    this.meForm.setErrors(null);
    this.isLoading = false;
  }

  private handleUpdateError(error: HttpErrorResponse): void {
    if (error.status === 409) {
      this.meForm.setErrors({ userAlreadyExists: true });
    } else if (error.status === 400) {
      this.meForm.setErrors({ validationError: true });
    } else {
      this.hasUpdateFailed = true;
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
