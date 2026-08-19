import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ProductPrice {
  id: number;
  productId: number;
  mrp: number;
  sellingPrice: number;
  active: boolean;
  validFrom: string;
  validTo: string | null;
  createdAt: string;
}

export interface SetPriceRequest {
  mrp: number;
  sellingPrice: number;
}

@Injectable({
  providedIn: 'root'
})
export class ProductPricingService {

  private readonly apiUrl = 'http://localhost:8081/api/v1/admin/products';

  constructor(private http: HttpClient) { }

  getCurrentPrice(productId: number): Observable<ProductPrice> {
    return this.http.get<ProductPrice>(`${this.apiUrl}/${productId}/price/current`);
  }

  getPriceHistory(productId: number): Observable<ProductPrice[]> {
    return this.http.get<ProductPrice[]>(`${this.apiUrl}/${productId}/price/history`);
  }

  setPrice(productId: number, request: SetPriceRequest): Observable<ProductPrice> {
    return this.http.post<ProductPrice>(`${this.apiUrl}/${productId}/price`, request);
  }
}
