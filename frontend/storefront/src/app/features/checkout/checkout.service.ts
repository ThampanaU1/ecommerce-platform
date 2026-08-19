import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AddressRequest {
  label: string;
  line1: string;
  line2: string;
  city: string;
  state: string;
  pincode: string;
  country: string;
}

export interface CheckoutItem {
  productId: number;
  quantity: number;
}

export interface CheckoutRequest {
  shippingAddress: AddressRequest;
  items: CheckoutItem[];
  couponCode: string | null;
}

export interface OrderItemResponse {
  productName: string;
  sku: string;
  unitPrice: number;
  quantity: number;
  lineTotal: number;
}

export interface OrderResponse {
  id: number;
  orderNumber: string;
  status: string;
  subtotal: number;
  discountTotal: number;
  taxTotal: number;
  shippingTotal: number;
  grandTotal: number;
  placedAt: string;
  items: OrderItemResponse[];
}

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {

  private readonly apiUrl = 'http://localhost:8081/api/v1/orders';
  private readonly couponUrl = 'http://localhost:8081/api/v1/coupons';

  constructor(private http: HttpClient) { }

  placeOrder(request: CheckoutRequest): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(this.apiUrl, request);
  }

  getMyOrders(): Observable<OrderResponse[]> {
    return this.http.get<OrderResponse[]>(this.apiUrl);
  }

  validateCoupon(code: string, orderSubtotal: number): Observable<{ discount: number }> {
    return this.http.post<{ discount: number }>(`${this.couponUrl}/validate`, { code, orderSubtotal });
  }
}
