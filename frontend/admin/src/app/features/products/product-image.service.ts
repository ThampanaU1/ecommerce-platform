import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface ProductImage {
  id: number;
  productId: number;
  imageUrl: string;
  isPrimary: boolean;
  displayOrder: number;
}

export interface ProductImageRequest {
  imageUrl: string;
  isPrimary: boolean;
  displayOrder: number;
}

@Injectable({
  providedIn: 'root'
})
export class ProductImageService {

  private readonly apiUrl = `${environment.apiUrl}/admin/products`;

  constructor(private http: HttpClient) { }

  getImages(productId: number): Observable<ProductImage[]> {
    return this.http.get<ProductImage[]>(`${this.apiUrl}/${productId}/images`);
  }

  addImage(productId: number, request: ProductImageRequest): Observable<ProductImage> {
    return this.http.post<ProductImage>(`${this.apiUrl}/${productId}/images`, request);
  }

  setPrimary(productId: number, imageId: number): Observable<ProductImage> {
    return this.http.patch<ProductImage>(`${this.apiUrl}/${productId}/images/${imageId}/primary`, {});
  }

  deleteImage(productId: number, imageId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${productId}/images/${imageId}`);
  }
}
