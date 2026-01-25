import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CustomerService } from '../services/customer.service';
import { RegisterCustomerRequest } from '../models/customer.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registrationForm: FormGroup;
  isLoading = false;
  errorMessage = '';
  successMessage = '';
  fieldErrors: { [key: string]: string } = {};

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private router: Router
  ) {
    this.registrationForm = this.fb.group({
      consumerId: ['', [Validators.required]],
      customerName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      mobileNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      address: [''],
      userId: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get f() {
    return this.registrationForm.controls;
  }

  onSubmit() {
    this.errorMessage = '';
    this.successMessage = '';
    this.fieldErrors = {};

    if (this.registrationForm.invalid) {
      this.markFormGroupTouched(this.registrationForm);
      return;
    }

    this.isLoading = true;

    const request: RegisterCustomerRequest = {
      consumerId: this.registrationForm.value.consumerId,
      customerName: this.registrationForm.value.customerName,
      email: this.registrationForm.value.email,
      mobileNumber: this.registrationForm.value.mobileNumber,
      address: this.registrationForm.value.address || '',
      userId: this.registrationForm.value.userId,
      password: this.registrationForm.value.password
    };

    this.customerService.registerCustomer(request).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = response.message || 'Customer registered successfully!';
        this.registrationForm.reset();
        
        // Redirect to home after 3 seconds
        setTimeout(() => {
          this.router.navigate(['/home']);
        }, 3000);
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
        
        // Check for duplicate email error
        if (apiError.message.includes('Email already exists') || 
            apiError.message.includes('Duplicate Email')) {
          this.registrationForm.get('email')?.setErrors({ duplicate: true });
        }
        return;
      }
    }
    
    // Generic error message
    this.errorMessage = 'An error occurred during registration. Please try again.';
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
    const control = this.registrationForm.get(fieldName);
    
    if (this.fieldErrors[fieldName]) {
      return this.fieldErrors[fieldName];
    }
    
    if (control?.hasError('required') && control.touched) {
      return `${this.getFieldLabel(fieldName)} is required`;
    }
    
    if (control?.hasError('email') && control.touched) {
      return 'Invalid email format';
    }
    
    if (control?.hasError('pattern') && control.touched) {
      if (fieldName === 'mobileNumber') {
        return 'Mobile number must be 10 digits';
      }
    }
    
    if (control?.hasError('minlength') && control.touched) {
      return 'Password must be at least 6 characters';
    }
    
    if (control?.hasError('duplicate') && control.touched) {
      return 'Email already exists';
    }
    
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      consumerId: 'Consumer ID',
      customerName: 'Customer Name',
      email: 'Email',
      mobileNumber: 'Mobile Number',
      userId: 'User ID',
      password: 'Password'
    };
    return labels[fieldName] || fieldName;
  }

  isFieldInvalid(fieldName: string): boolean {
    const control = this.registrationForm.get(fieldName);
    return !!(control && control.invalid && control.touched);
  }
}
