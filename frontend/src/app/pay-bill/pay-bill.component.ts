import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { PaymentService } from '../services/payment.service';
import { BillService } from '../services/bill.service';
import { PayBillRequest } from '../models/payment.model';
import { BillResponse } from '../models/bill.model';

@Component({
  selector: 'app-pay-bill',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterModule],
  templateUrl: './pay-bill.component.html',
  styleUrl: './pay-bill.component.css'
})
export class PayBillComponent implements OnInit {
  paymentForm: FormGroup;
  isLoading = false;
  isLoadingBill = false;
  errorMessage = '';
  successMessage = '';
  fieldErrors: { [key: string]: string } = {};
  bill: BillResponse | null = null;
  billId: number | null = null;
  paymentResponse: any = null;

  constructor(
    private fb: FormBuilder,
    private paymentService: PaymentService,
    private billService: BillService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.paymentForm = this.fb.group({
      billId: ['', [Validators.required]],
      amount: ['', [Validators.required, Validators.min(0.01)]],
      paymentMethod: ['CARD'],
      cardNumber: ['', [Validators.required, Validators.pattern(/^\d{13,19}$/)]],
      cardExpiry: ['', [Validators.required, Validators.pattern(/^(0[1-9]|1[0-2])\/(2[0-9]|3[0-9])$/)]],
      cardCvv: ['', [Validators.required, Validators.pattern(/^\d{3,4}$/)]],
      transactionReference: ['']
    });
  }

  /** Luhn algorithm for card number validation */
  private luhnCheck(cardNumber: string): boolean {
    const digits = cardNumber.replace(/\D/g, '');
    if (digits.length < 13 || digits.length > 19) return false;
    let sum = 0;
    let isEven = false;
    for (let i = digits.length - 1; i >= 0; i--) {
      let digit = parseInt(digits[i], 10);
      if (isEven) {
        digit *= 2;
        if (digit > 9) digit -= 9;
      }
      sum += digit;
      isEven = !isEven;
    }
    return sum % 10 === 0;
  }

  get isCardNumberValid(): boolean {
    const cardNumber = this.paymentForm?.get('cardNumber')?.value;
    if (!cardNumber || cardNumber.length < 13) return false;
    return this.luhnCheck(cardNumber);
  }

  ngOnInit() {
    // Get billId from query params
    this.route.queryParams.subscribe(params => {
      this.billId = params['billId'] ? Number(params['billId']) : null;
      if (this.billId) {
        this.paymentForm.patchValue({ billId: this.billId });
        this.loadBillDetails();
      }
    });
  }

  loadBillDetails() {
    if (!this.billId) return;

    this.isLoadingBill = true;
    this.errorMessage = '';

    // Get consumerId from session or query params
    const consumerId = this.route.snapshot.queryParams['consumerId'] || 
                      sessionStorage.getItem('consumerId') || '';

    if (consumerId) {
      this.billService.viewBills(consumerId).subscribe({
        next: (bills) => {
          this.isLoadingBill = false;
          this.bill = bills.find(b => b.billId === this.billId) || null;
          
          if (this.bill) {
            // Pre-fill amount with bill amount
            this.paymentForm.patchValue({
              amount: this.bill.amount
            });
            
            // Check if bill is already paid
            if (this.bill.status === 'PAID') {
              this.errorMessage = 'This bill has already been paid.';
            }
          } else {
            this.errorMessage = 'Bill not found.';
          }
        },
        error: (error) => {
          this.isLoadingBill = false;
          this.handleError(error);
        }
      });
    } else {
      this.isLoadingBill = false;
      this.errorMessage = 'Consumer ID is required to load bill details.';
    }
  }

  onSubmit() {
    this.errorMessage = '';
    this.successMessage = '';
    this.fieldErrors = {};
    this.paymentResponse = null;

    if (this.paymentForm.invalid) {
      this.markFormGroupTouched(this.paymentForm);
      return;
    }

    // Validate card number with Luhn algorithm
    const cardNumber = this.paymentForm.value.cardNumber?.replace(/\s/g, '') || '';
    if (!this.luhnCheck(cardNumber)) {
      this.paymentForm.get('cardNumber')?.setErrors({ luhn: true });
      this.errorMessage = 'Invalid card number. Please check and try again.';
      return;
    }

    // Validate amount doesn't exceed bill amount
    if (this.bill && this.paymentForm.value.amount > this.bill.amount) {
      this.errorMessage = 'Payment amount cannot exceed bill amount.';
      return;
    }

    this.isLoading = true;

    // Send last 4 digits as transaction reference (no full card stored)
    const lastFour = cardNumber.slice(-4);
    const request: PayBillRequest = {
      billId: this.paymentForm.value.billId,
      amount: this.paymentForm.value.amount,
      paymentMethod: 'CARD',
      transactionReference: `Card ****${lastFour}`
    };

    this.paymentService.payBill(request).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.paymentResponse = response;
        this.successMessage = response.message || 'Payment successful!';
        
        // Update bill status locally
        if (this.bill) {
          this.bill.status = 'PAID';
        }
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
        
        // Check for bill already paid
        if (apiError.message.includes('already paid') || 
            apiError.message.includes('Bill Already Paid')) {
          if (this.bill) {
            this.bill.status = 'PAID';
          }
        }
        
        // Check for bill not found
        if (apiError.message.includes('not found') || 
            apiError.message.includes('Bill Not Found')) {
          this.bill = null;
        }
        
        return;
      }
    }
    
    // Generic error message
    this.errorMessage = 'An error occurred during payment processing. Please try again.';
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
    const control = this.paymentForm.get(fieldName);
    
    if (this.fieldErrors[fieldName]) {
      return this.fieldErrors[fieldName];
    }
    
    if (control?.hasError('required') && control.touched) {
      return `${this.getFieldLabel(fieldName)} is required`;
    }
    
    if (control?.hasError('min') && control.touched) {
      return 'Amount must be greater than 0';
    }

    if (control?.hasError('luhn') && control.touched) {
      return 'Invalid card number (failed checksum)';
    }

    if (control?.hasError('pattern') && control.touched) {
      if (fieldName === 'cardNumber') return 'Card number must be 13–19 digits';
      if (fieldName === 'cardExpiry') return 'Use MM/YY (e.g. 12/28)';
      if (fieldName === 'cardCvv') return 'CVV must be 3 or 4 digits';
    }
    
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      billId: 'Bill ID',
      amount: 'Amount',
      paymentMethod: 'Payment Method',
      cardNumber: 'Card Number',
      cardExpiry: 'Expiry',
      cardCvv: 'CVV',
      transactionReference: 'Transaction Reference'
    };
    return labels[fieldName] || fieldName;
  }

  isFieldInvalid(fieldName: string): boolean {
    const control = this.paymentForm.get(fieldName);
    return !!(control && control.invalid && control.touched);
  }

  onCardNumberInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const value = input.value.replace(/\D/g, '');
    input.value = value;
    this.paymentForm.patchValue({ cardNumber: value }, { emitEvent: true });
  }

  onCardExpiryInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    let value = input.value.replace(/\D/g, '');
    if (value.length >= 2) {
      value = value.slice(0, 2) + '/' + value.slice(2, 4);
    }
    input.value = value;
    this.paymentForm.patchValue({ cardExpiry: value }, { emitEvent: true });
  }

  onCardCvvInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    input.value = input.value.replace(/\D/g, '').slice(0, 4);
    this.paymentForm.patchValue({ cardCvv: input.value }, { emitEvent: true });
  }

  formatCurrency(amount: number): string {
    if (!amount) return '₹0.00';
    return `₹${amount.toFixed(2)}`;
  }

  formatDate(dateString: string): string {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { 
      year: 'numeric', 
      month: 'short', 
      day: 'numeric' 
    });
  }

  onBillIdChange() {
    this.billId = this.paymentForm.value.billId;
    if (this.billId) {
      this.loadBillDetails();
    } else {
      this.bill = null;
    }
  }

  get maxAmount(): number | null {
    return this.bill?.amount ?? null;
  }
}
