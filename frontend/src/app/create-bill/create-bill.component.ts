import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { BillService } from '../services/bill.service';
import { CreateBillRequest } from '../models/bill.model';

@Component({
  selector: 'app-create-bill',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './create-bill.component.html',
  styleUrl: './create-bill.component.css'
})
export class CreateBillComponent {
  billForm: FormGroup;
  isLoading = false;
  errorMessage = '';
  successMessage = '';
  fieldErrors: { [key: string]: string } = {};

  constructor(
    private fb: FormBuilder,
    private billService: BillService,
    private router: Router
  ) {
    this.billForm = this.fb.group({
      consumerId: ['', [Validators.required]],
      billDate: ['', [Validators.required]],
      dueDate: ['', [Validators.required]],
      unitsConsumed: ['', [Validators.required, Validators.min(0.01)]],
      amount: ['', [Validators.required, Validators.min(0.01)]],
      description: ['']
    });
  }

  get f() {
    return this.billForm.controls;
  }

  onSubmit() {
    this.errorMessage = '';
    this.successMessage = '';
    this.fieldErrors = {};

    if (this.billForm.invalid) {
      this.markFormGroupTouched(this.billForm);
      return;
    }

    // Validate dates
    const billDate = new Date(this.billForm.value.billDate);
    const dueDate = new Date(this.billForm.value.dueDate);
    
    if (dueDate < billDate) {
      this.errorMessage = 'Due date cannot be before bill date';
      return;
    }

    this.isLoading = true;

    const request: CreateBillRequest = {
      consumerId: this.billForm.value.consumerId,
      billDate: this.billForm.value.billDate,
      dueDate: this.billForm.value.dueDate,
      unitsConsumed: parseFloat(this.billForm.value.unitsConsumed),
      amount: parseFloat(this.billForm.value.amount),
      description: this.billForm.value.description || undefined
    };

    this.billService.createBill(request).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = `Bill created successfully! Bill ID: ${response.billId}`;
        this.billForm.reset();
        
        // Redirect to bills page after 3 seconds
        setTimeout(() => {
          this.router.navigate(['/bills']);
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
        return;
      }
    }
    
    // Generic error message
    this.errorMessage = 'An error occurred while creating the bill. Please try again.';
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
    const control = this.billForm.get(fieldName);
    
    if (this.fieldErrors[fieldName]) {
      return this.fieldErrors[fieldName];
    }
    
    if (control?.hasError('required') && control.touched) {
      return `${this.getFieldLabel(fieldName)} is required`;
    }
    
    if (control?.hasError('min') && control.touched) {
      return `${this.getFieldLabel(fieldName)} must be greater than 0`;
    }
    
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      consumerId: 'Consumer ID',
      billDate: 'Bill Date',
      dueDate: 'Due Date',
      unitsConsumed: 'Units Consumed',
      amount: 'Amount',
      description: 'Description'
    };
    return labels[fieldName] || fieldName;
  }

  isFieldInvalid(fieldName: string): boolean {
    const control = this.billForm.get(fieldName);
    return !!(control && control.invalid && control.touched);
  }
}
