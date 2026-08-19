import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private readonly apiUrl = 'http://localhost:8081/api/v1/admin/reviews';

  constructor(private http: HttpClient) { }

  getAll(): Observable<Review[]> {
    return this.http.get<Review[]>(this.apiUrl);
  }

  updateStatus(id: number, status: string): Observable<Review> {
    return this.http.patch<Review>(`${this.apiUrl}/${id}/status`, { status });
  }
}
