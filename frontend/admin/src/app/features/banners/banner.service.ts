import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Banner {
  id: number;
  title: string;
  imageUrl: string;
  linkUrl: string;
  displayOrder: number;
  isActive: boolean;
}

export interface BannerRequest {
  title: string;
  imageUrl: string;
  linkUrl: string;
  displayOrder: number;
}

@Injectable({
  providedIn: 'root'
})
export class BannerService {
  private readonly apiUrl = `${environment.apiUrl}/admin/banners`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<Banner[]> {
    return this.http.get<Banner[]>(this.apiUrl);
  }

  create(request: BannerRequest): Observable<Banner> {
    return this.http.post<Banner>(this.apiUrl, request);
  }

  setStatus(id: number, active: boolean): Observable<Banner> {
    return this.http.patch<Banner>(`${this.apiUrl}/${id}/status`, { active });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
