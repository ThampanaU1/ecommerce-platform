import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface AdminUser {
  id: number;
  name: string;
  email: string;
  status: string;
  roles: string[];
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly apiUrl = `${environment.apiUrl}/admin/users`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<AdminUser[]> {
    return this.http.get<AdminUser[]>(this.apiUrl);
  }

  updateRoles(userId: number, roles: string[]): Observable<AdminUser> {
    return this.http.put<AdminUser>(`${this.apiUrl}/${userId}/roles`, { roles });
  }

  updateStatus(userId: number, status: string): Observable<AdminUser> {
    return this.http.patch<AdminUser>(`${this.apiUrl}/${userId}/status`, { status });
  }
}
