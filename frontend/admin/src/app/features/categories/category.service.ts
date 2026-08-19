import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Category {
  id: number;
  name: string;
  description: string;
  imageUrl: string;
  displayOrder: number;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CategoryRequest {
  name: string;
  description: string;
  imageUrl: string;
  displayOrder: number;
}

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private readonly apiUrl = 'http://localhost:8081/api/v1/admin/categories';

  constructor(private http: HttpClient) { }

  getAll(): Observable<Category[]> {
    return this.http.get<Category[]>(this.apiUrl);
  }

  getById(id: number): Observable<Category> {
    return this.http.get<Category>(`${this.apiUrl}/${id}`);
  }

  create(request: CategoryRequest): Observable<Category> {
    return this.http.post<Category>(this.apiUrl, request);
  }

  update(id: number, request: CategoryRequest): Observable<Category> {
    return this.http.put<Category>(`${this.apiUrl}/${id}`, request);
  }

  setStatus(id: number, active: boolean): Observable<Category> {
    return this.http.patch<Category>(`${this.apiUrl}/${id}/status`, { active });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
