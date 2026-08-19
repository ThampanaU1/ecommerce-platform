import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface OrderItem {
  productName: string;
  sku: string;
  unitPrice: number;
  quantity: number;
  lineTotal: number;
}

export interface AdminOrder {
  id: number;
  orderNumber: string;
  customerName: string;
  customerEmail: string;
  status: string;
  subtotal: number;
  taxTotal: number;
  shippingTotal: number;
  grandTotal: number;
  placedAt: string;
  shippingCity: string;
  shippingState: string;
  items: OrderItem[];
}

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private readonly apiUrl = 'http://localhost:8081/api/v1/admin/orders';

  constructor(private http: HttpClient) { }

  getAll(): Observable<AdminOrder[]> {
    return this.http.get<AdminOrder[]>(this.apiUrl);
  }

  getById(id: number): Observable<AdminOrder> {
    return this.http.get<AdminOrder>(`${this.apiUrl}/${id}`);
  }

  updateStatus(id: number, status: string): Observable<AdminOrder> {
    return this.http.patch<AdminOrder>(`${this.apiUrl}/${id}/status`, { status });
  }
}
