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
  featured: boolean;
  mrp: number | null;
  sellingPrice: number | null;
  availableQuantity: number;
  primaryImageUrl: string | null;
}

export interface Category {
  id: number;
  name: string;
  description: string;
  imageUrl: string;
  displayOrder: number;
}

export interface Banner {
  id: number;
  title: string;
  imageUrl: string;
  linkUrl: string;
  displayOrder: number;
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

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private readonly apiUrl = 'http://localhost:8081/api/v1';

  constructor(private http: HttpClient) { }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/products`);
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/products/${id}`);
  }

  getProductsByCategory(categoryId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/categories/${categoryId}/products`);
  }

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiUrl}/categories`);
  }

  getBanners(): Observable<Banner[]> {
    return this.http.get<Banner[]>(`${this.apiUrl}/banners`);
  }

  getReviews(productId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.apiUrl}/products/${productId}/reviews`);
  }

  getProductImages(productId: number): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/products/${productId}/images`);
  }
  submitReview(productId: number, request: ReviewRequest): Observable<Review> {
    return this.http.post<Review>(`${this.apiUrl}/products/${productId}/reviews`, request);
  }
}
