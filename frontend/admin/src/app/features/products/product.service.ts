import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Product {
  id: number;
  sku: string;
  name: string;
  description: string;
  categoryId: number;
  categoryName: string;
  status: string;
  featured: boolean;
  createdAt: string;
  updatedAt: string;
}
export interface Review {
  id: number;
  productId: number;
  productName: string;
  customerName: string;
  rating: number;
  comment: string;
  status: string;
  createdAt: string;
}

export interface ReviewRequest {
  rating: number;
  comment: string;
}

export interface ProductRequest {
  sku: string;
  name: string;
  description: string;
  categoryId: number;
  featured: boolean;
}
export interface Banner {
  id: number;
  title: string;
  imageUrl: string;
  linkUrl: string;
  displayOrder: number;
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private readonly apiUrl = 'http://localhost:8081/api/v1/admin/products';

  constructor(private http: HttpClient) { }

  getReviews(productId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.apiUrl}/products/${productId}/reviews`);
  }

  submitReview(productId: number, request: ReviewRequest): Observable<Review> {
    return this.http.post<Review>(`${this.apiUrl}/products/${productId}/reviews`, request);
  }
  getAll(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }
  getBanners(): Observable<Banner[]> {
    return this.http.get<Banner[]>(`${this.apiUrl}/banners`);
  }

  getById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }

  create(request: ProductRequest): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, request);
  }

  update(id: number, request: ProductRequest): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, request);
  }

  setStatus(id: number, status: string): Observable<Product> {
    return this.http.patch<Product>(`${this.apiUrl}/${id}/status`, { status });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
