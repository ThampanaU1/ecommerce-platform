import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Inventory {
  id: number;
  productId: number;
  availableQuantity: number;
  reservedQuantity: number;
  reorderLevel: number;
  updatedAt: string;
}

export interface SetInventoryRequest {
  availableQuantity: number;
  reorderLevel: number;
}

export interface AdjustStockRequest {
  quantityChange: number;
}

@Injectable({
  providedIn: 'root'
})
export class ProductInventoryService {

  private readonly apiUrl = 'http://localhost:8081/api/v1/admin/products';

  constructor(private http: HttpClient) { }

  getInventory(productId: number): Observable<Inventory> {
    return this.http.get<Inventory>(`${this.apiUrl}/${productId}/inventory`);
  }

  setInventory(productId: number, request: SetInventoryRequest): Observable<Inventory> {
    return this.http.post<Inventory>(`${this.apiUrl}/${productId}/inventory`, request);
  }

  adjustStock(productId: number, request: AdjustStockRequest): Observable<Inventory> {
    return this.http.patch<Inventory>(`${this.apiUrl}/${productId}/inventory/adjust`, request);
  }
}
