import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BillResponse } from '../models/bill.model';

@Injectable({
  providedIn: 'root'
})
export class BillService {
  private apiUrl = 'http://localhost:8080/api/bills';

  constructor(private http: HttpClient) {}

  viewBills(consumerId: string): Observable<BillResponse[]> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.http.get<BillResponse[]>(`${this.apiUrl}/viewBills?consumerId=${consumerId}`, { headers });
  }

  viewAllBills(): Observable<BillResponse[]> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.http.get<BillResponse[]>(`${this.apiUrl}`, { headers });
  }
}
