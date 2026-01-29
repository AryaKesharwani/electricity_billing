import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AdminService } from '../services/admin.service';
import { CustomerListItem, RegisterCustomerRequest } from '../models/customer.model';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {
  userEmail: string = '';
  customers: CustomerListItem[] = [];
  isLoadingCustomers = false;
  createForm: FormGroup;
  isCreating = false;
  createSuccess = '';
  createError = '';
  deletingId: string | null = null;

  constructor(
    private adminService: AdminService,
    private fb: FormBuilder
  ) {
    this.createForm = this.fb.group({
      consumerId: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      customerName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      email: ['', [Validators.required, Validators.email]],
      mobileNumber: ['', [Validators.required, Validators.pattern(/^[6-9][0-9]{9}$/)]],
      address: ['', [Validators.maxLength(500)]],
      userId: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(50)]]
    });
  }

  ngOnInit() {
    this.userEmail = sessionStorage.getItem('email') || '';
    this.loadCustomers();
  }

  loadCustomers() {
    this.isLoadingCustomers = true;
    this.adminService.getCustomers().subscribe({
      next: (list) => {
        this.customers = list;
        this.isLoadingCustomers = false;
      },
      error: () => {
        this.isLoadingCustomers = false;
      }
    });
  }

  onSubmitCreate() {
    this.createSuccess = '';
    this.createError = '';
    if (this.createForm.invalid) {
      this.createForm.markAllAsTouched();
      return;
    }
    this.isCreating = true;
    const req: RegisterCustomerRequest = {
      consumerId: this.createForm.value.consumerId,
      customerName: this.createForm.value.customerName,
      email: this.createForm.value.email,
      mobileNumber: this.createForm.value.mobileNumber,
      address: this.createForm.value.address || '',
      userId: this.createForm.value.userId,
      password: this.createForm.value.password
    };
    this.adminService.createConsumer(req).subscribe({
      next: () => {
        this.isCreating = false;
        this.createSuccess = 'Consumer created successfully.';
        this.createForm.reset();
        this.loadCustomers();
      },
      error: (err) => {
        this.isCreating = false;
        this.createError = err.error?.message || 'Failed to create consumer.';
      }
    });
  }

  deleteCustomer(consumerId: string) {
    if (!confirm('Delete this consumer and all their bills/payments? This cannot be undone.')) {
      return;
    }
    this.deletingId = consumerId;
    this.adminService.deleteCustomer(consumerId).subscribe({
      next: () => {
        this.deletingId = null;
        this.loadCustomers();
      },
      error: () => {
        this.deletingId = null;
      }
    });
  }

  logout() {
    localStorage.removeItem('token');
    sessionStorage.clear();
    window.location.href = '/home';
  }
}
