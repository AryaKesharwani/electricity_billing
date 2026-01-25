import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { LoginRequest } from '../models/auth.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  isLoading = false;
  errorMessage = '';
  successMessage = '';
  fieldErrors: { [key: string]: string } = {};

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      userName: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.errorMessage = '';
    this.successMessage = '';
    this.fieldErrors = {};

    if (this.loginForm.invalid) {
      this.markFormGroupTouched(this.loginForm);
      return;
    }

    this.isLoading = true;

    const request: LoginRequest = {
      userName: this.loginForm.value.userName,
      password: this.loginForm.value.password
    };

    this.authService.validateLogin(request).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = response.message || 'Login successful!';
        
        // Store user info and JWT token
        if (response.userType && response.token) {
          // Store JWT token
          localStorage.setItem('token', response.token);
          sessionStorage.setItem('token', response.token);
          
          // Store user info
          sessionStorage.setItem('userType', response.userType);
          sessionStorage.setItem('email', response.email);
          sessionStorage.setItem('status', response.status);
          
          // Store userName as consumerId if it looks like a consumer ID (starts with CUST or is numeric)
          const userName = this.loginForm.value.userName;
          if (response.userType === 'CUSTOMER' && (userName.startsWith('CUST') || /^\d+$/.test(userName))) {
            sessionStorage.setItem('consumerId', userName);
          }
        }
        
        // Redirect based on user type
        setTimeout(() => {
          if (response.userType === 'ADMIN') {
            this.router.navigate(['/admin/dashboard']);
          } else {
            this.router.navigate(['/customer/dashboard']);
          }
        }, 1500);
      },
      error: (error) => {
        this.isLoading = false;
        this.handleError(error);
      }
    });
  }

  private handleError(error: any) {
    if (error.error) {
      const apiError = error.error;
      
      // Handle validation errors
      if (apiError.errors) {
        this.fieldErrors = apiError.errors;
        this.errorMessage = 'Please correct the errors in the form.';
        return;
      }
      
      // Handle specific error messages
      if (apiError.message) {
        this.errorMessage = apiError.message;
        
        // Check for invalid credentials
        if (apiError.message.includes('Invalid username or password') || 
            apiError.message.includes('Invalid Credentials')) {
          // Clear password field for security
          this.loginForm.get('password')?.reset();
        }
        
        // Check for deactivated account
        if (apiError.message.includes('deactivated') || 
            apiError.message.includes('Account Deactivated')) {
          this.errorMessage = 'Your account has been deactivated. Please contact the administrator.';
        }
        
        return;
      }
    }
    
    // Generic error message
    this.errorMessage = 'An error occurred during login. Please try again.';
  }

  private markFormGroupTouched(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();

      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  getFieldError(fieldName: string): string {
    const control = this.loginForm.get(fieldName);
    
    if (this.fieldErrors[fieldName]) {
      return this.fieldErrors[fieldName];
    }
    
    if (control?.hasError('required') && control.touched) {
      return `${this.getFieldLabel(fieldName)} is required`;
    }
    
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      userName: 'User Name',
      password: 'Password'
    };
    return labels[fieldName] || fieldName;
  }

  isFieldInvalid(fieldName: string): boolean {
    const control = this.loginForm.get(fieldName);
    return !!(control && control.invalid && control.touched);
  }
}
