// shared/validators/password.validator.ts
import { AbstractControl, ValidationErrors } from '@angular/forms';

export function passwordValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value;
  if (!value) return null;

  const hasUpperCase = /[A-Z]/.test(value);
  const hasLowerCase = /[a-z]/.test(value);
  const hasNumeric = /[0-9]/.test(value);
  const hasSpecialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(value);

  return hasUpperCase && hasLowerCase && hasNumeric && hasSpecialChar ? null : { passwordComplexity: true };
}
