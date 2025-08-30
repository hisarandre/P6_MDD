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
import { Subject, finalize, takeUntil, catchError, tap, of, switchMap } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { MatIconModule } from '@angular/material/icon';
import { AsyncPipe, NgIf } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import {SubHeaderComponent} from "../../../../layout/components/sub-header/sub-header.component";
import {passwordValidator} from "../../../../shared/validators/password.validators";

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.scss'],
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    RouterLink,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    SubHeaderComponent
  ]
})
export class RegisterPageComponent implements OnInit, OnDestroy {
  public registerForm!: FormGroup;

  public isRegistering = false;
  public isAutoLoggingIn = false;

  public hasRegistrationFailed = false;
  public hasLoginFailed = false;

  private destroy$ = new Subject<void>();

  constructor(
    public readonly router: Router,
    private readonly formBuilder: FormBuilder,
    private readonly authService: AuthService
  ) {}

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(100), passwordValidator]],
    });
  }

  public get username() { return this.registerForm.get('username'); }
  public get email() { return this.registerForm.get('email'); }
  public get password() { return this.registerForm.get('password'); }

  public get isLoading(): boolean {
    return this.isRegistering || this.isAutoLoggingIn;
  }

  public get loadingMessage(): string {
    if (this.isRegistering) return 'Création du compte...';
    if (this.isAutoLoggingIn) return 'Connexion en cours...';
    return "S'inscrire";
  }

  public goBack(): void {
    if (!this.isLoading) {
      this.router.navigate(['/login']).catch(console.error);
    }
  }

  public onRegister(): void {
    if (this.registerForm.invalid || this.isLoading) return;

    this.resetState();
    this.isRegistering = true;

    const registerRequest: RegisterRequest = this.registerForm.value;

    this.authService.register(registerRequest)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.handleRegistrationError(error);
          return of(null);
        }),
        tap((token) => {
          if (token) {
            this.isRegistering = false;
            this.isAutoLoggingIn = true;
          }
        }),
        switchMap((token: string | null) => {
          if (!token) return of(null);
          return this.authService.performAutoLogin(token).pipe(
            catchError((loginError: HttpErrorResponse) => {
              this.handleLoginError(loginError);
              return of(null);
            })
          );
        }),
        takeUntil(this.destroy$),
        finalize(() => {
          this.isRegistering = false;
          this.isAutoLoggingIn = false;
        })
      )
      .subscribe({
        next: (user) => {
          if (user) this.router.navigate(['/feed']).catch(console.error);
        }
      });
  }

  private resetState(): void {
    this.hasRegistrationFailed = false;
    this.hasLoginFailed = false;
    this.registerForm.setErrors(null);
    this.isRegistering = false;
    this.isAutoLoggingIn = false;
  }

  private handleRegistrationError(error: HttpErrorResponse): void {
    if (error.status === 409) {
      this.registerForm.setErrors({ userAlreadyExists: true });
    } else {
      this.hasRegistrationFailed = true;
    }
  }

  private handleLoginError(error: HttpErrorResponse): void {
    this.hasLoginFailed = true;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
