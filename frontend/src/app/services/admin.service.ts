import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomerListItem } from '../models/customer.model';
import { RegisterCustomerRequest, RegisterCustomerResponse } from '../models/customer.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  getCustomers(): Observable<CustomerListItem[]> {
    return this.http.get<CustomerListItem[]>(`${this.apiUrl}/customers`);
  }

  createConsumer(request: RegisterCustomerRequest): Observable<RegisterCustomerResponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.http.post<RegisterCustomerResponse>(`${this.apiUrl}/customers`, request, { headers });
  }

  deleteCustomer(consumerId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/customers/${encodeURIComponent(consumerId)}`);
  }
}
