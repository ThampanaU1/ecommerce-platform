import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Coupon {
  id: number;
  code: string;
  type: string;
  value: number;
  minOrderValue: number;
  maxDiscount: number | null;
  usageLimitTotal: number | null;
  usageLimitPerUser: number | null;
  startDate: string;
  endDate: string;
  isActive: boolean;
}

export interface CouponRequest {
  code: string;
  type: string;
  value: number;
  minOrderValue: number;
  maxDiscount: number | null;
  usageLimitTotal: number | null;
  usageLimitPerUser: number | null;
  startDate: string;
  endDate: string;
}

@Injectable({
  providedIn: 'root'
})
export class CouponService {

  private readonly apiUrl = 'http://localhost:8081/api/v1/admin/coupons';

  constructor(private http: HttpClient) { }

  getAll(): Observable<Coupon[]> {
    return this.http.get<Coupon[]>(this.apiUrl);
  }

  create(request: CouponRequest): Observable<Coupon> {
    return this.http.post<Coupon>(this.apiUrl, request);
  }

  setStatus(id: number, active: boolean): Observable<Coupon> {
    return this.http.patch<Coupon>(`${this.apiUrl}/${id}/status`, { active });
  }
}
