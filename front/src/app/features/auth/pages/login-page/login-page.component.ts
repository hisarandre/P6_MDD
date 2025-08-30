import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule
} from '@angular/forms';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { Subject, of, takeUntil, finalize, catchError, tap } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { MatIconModule } from '@angular/material/icon';
import { AsyncPipe, NgIf } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { SubHeaderComponent } from '../../../../layout/components/sub-header/sub-header.component';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss'],
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    SubHeaderComponent
  ]
})
export class LoginPageComponent implements OnInit, OnDestroy {
  public loginForm!: FormGroup;
  public isLoggingIn = false;
  public hasLoginFailed = false;
  private destroy$ = new Subject<void>();

  constructor(
    public readonly router: Router,
    private readonly formBuilder: FormBuilder,
    private readonly authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  public get email() { return this.loginForm.get('email'); }
  public get password() { return this.loginForm.get('password'); }

  public get isLoading(): boolean {
    return this.isLoggingIn;
  }

  public get loadingMessage(): string {
    return this.isLoggingIn ? 'Connexion en cours...' : 'Se connecter';
  }

  public goBack(): void {
    if (!this.isLoading) {
      this.router.navigate(['/']).catch(console.error);
    }
  }

  public onLogin(): void {
    if (this.loginForm.invalid || this.isLoading) return;

    this.hasLoginFailed = false;
    this.isLoggingIn = true;

    this.authService.login(this.loginForm.value)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.hasLoginFailed = true;
          return of(null);
        }),
        finalize(() => this.isLoggingIn = false),
        takeUntil(this.destroy$)
      )
      .subscribe(user => {
        if (user) this.router.navigate(['/posts']).catch(console.error);
      });

  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
