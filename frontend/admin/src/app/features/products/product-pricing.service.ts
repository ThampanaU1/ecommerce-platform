import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

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

  private readonly apiUrl = `${environment.apiUrl}/admin/products`;

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
