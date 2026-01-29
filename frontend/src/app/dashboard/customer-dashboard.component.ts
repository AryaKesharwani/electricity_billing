import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { BillService } from '../services/bill.service';
import { BillResponse } from '../models/bill.model';

@Component({
  selector: 'app-customer-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './customer-dashboard.component.html',
  styleUrl: './customer-dashboard.component.css'
})
export class CustomerDashboardComponent implements OnInit {
  userEmail: string = '';
  customerName: string = '';
  userType: string = '';
  consumerId: string = '';
  recentBills: BillResponse[] = [];
  isLoading = false;
  totalUnpaidBills = 0;
  totalAmountDue = 0;

  constructor(private billService: BillService) {}

  ngOnInit() {
    // Get user info from session storage
    this.userEmail = sessionStorage.getItem('email') || '';
    this.customerName = sessionStorage.getItem('customerName') || '';
    this.userType = sessionStorage.getItem('userType') || '';
    this.consumerId = sessionStorage.getItem('consumerId') || '';

    // If we have consumerId, load recent bills
    if (this.consumerId) {
      this.loadRecentBills();
    }
  }

  loadRecentBills() {
    if (!this.consumerId) {
      return;
    }
    
    // Save consumerId to session storage
    sessionStorage.setItem('consumerId', this.consumerId);
    
    this.isLoading = true;
    this.billService.viewBills(this.consumerId).subscribe({
      next: (bills) => {
        this.isLoading = false;
        this.recentBills = bills.slice(0, 5); // Get latest 5 bills
        this.totalUnpaidBills = bills.filter(b => b.status === 'UNPAID' || b.status === 'OVERDUE').length;
        this.totalAmountDue = bills
          .filter(b => b.status === 'UNPAID' || b.status === 'OVERDUE')
          .reduce((sum, bill) => sum + bill.amount, 0);
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Error loading bills:', error);
      }
    });
  }

  logout() {
    localStorage.removeItem('token');
    sessionStorage.clear();
    window.location.href = '/home';
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
}
