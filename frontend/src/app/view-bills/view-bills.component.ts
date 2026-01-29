import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { BillService } from '../services/bill.service';
import { BillResponse } from '../models/bill.model';

@Component({
  selector: 'app-view-bills',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './view-bills.component.html',
  styleUrl: './view-bills.component.css'
})
export class ViewBillsComponent implements OnInit {
  bills: BillResponse[] = [];
  isLoading = false;
  errorMessage = '';
  consumerId: string = '';
  hasBills = false;
  isAdmin = false;
  updatingBillId: number | null = null;

  constructor(
    private billService: BillService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.isAdmin = sessionStorage.getItem('userType') === 'ADMIN';
    // Get consumerId from query params or session storage
    this.route.queryParams.subscribe(params => {
      this.consumerId = params['consumerId'] || sessionStorage.getItem('consumerId') || '';
      // Load all bills on init - for admins, show all; for customers, show their bills
      this.loadBills();
    });
  }

  loadBills() {
    this.isLoading = true;
    this.errorMessage = '';
    this.bills = [];

    // If no consumerId, load all bills (mainly for admins)
    // If consumerId provided, search for that specific consumer
    const request = this.consumerId 
      ? this.billService.viewBills(this.consumerId)
      : this.billService.viewAllBills();

    request.subscribe({
      next: (response) => {
        this.isLoading = false;
        this.bills = response;
        this.hasBills = response.length > 0;
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
      
      if (apiError.message) {
        this.errorMessage = apiError.message;
        return;
      }
    }
    
    // Generic error message
    this.errorMessage = 'An error occurred while fetching bills. Please try again.';
  }

  getStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'PAID':
        return 'status-paid';
      case 'UNPAID':
        return 'status-unpaid';
      case 'OVERDUE':
        return 'status-overdue';
      default:
        return '';
    }
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

  formatCurrency(amount: number): string {
    if (!amount) return '₹0.00';
    return `₹${amount.toFixed(2)}`;
  }

  onConsumerIdChange() {
    // Load bills with current consumerId (empty = all bills)
    this.loadBills();
  }

  clearSearch() {
    this.consumerId = '';
    this.loadBills();
  }

  changeStatus(bill: BillResponse, newStatus: string) {
    if (bill.status === newStatus) return;
    this.updatingBillId = bill.billId;
    this.billService.updateBillStatus(bill.billId, newStatus).subscribe({
      next: (updated) => {
        bill.status = updated.status;
        this.updatingBillId = null;
      },
      error: () => {
        this.updatingBillId = null;
      }
    });
  }
}
