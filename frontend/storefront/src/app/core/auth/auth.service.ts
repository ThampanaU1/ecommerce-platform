import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  userId: number;
  name: string;
  email: string;
  roles: string[];
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  getProfile(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${this.apiUrl}/profile`);
  }

  updateProfile(name: string): Observable<AuthResponse> {
    return this.http.put<AuthResponse>(`${this.apiUrl}/profile`, { name }).pipe(
      tap(response => {
        if (this.isBrowser) {
          const stored = localStorage.getItem('user');
          if (stored) {
            const user = JSON.parse(stored);
            user.name = response.name;
            localStorage.setItem('user', JSON.stringify(user));
          }
        }
      })
    );
  }


  private readonly apiUrl = `${environment.apiUrl}/auth`;
  private isBrowser: boolean;

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request).pipe(
      tap(response => this.storeSession(response))
    );
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request).pipe(
      tap(response => this.storeSession(response))
    );
  }

  logout(): void {
    if (this.isBrowser) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    }
  }
  isAdmin(): boolean {
    if (!this.isBrowser) return false;
    const stored = localStorage.getItem('user');
    if (!stored) return false;
    const user = JSON.parse(stored);
    return user.roles && user.roles.includes('ADMIN');
  }
  getToken(): string | null {
    return this.isBrowser ? localStorage.getItem('token') : null;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  private storeSession(response: AuthResponse): void {
    if (this.isBrowser) {
      localStorage.setItem('token', response.token);
      localStorage.setItem('user', JSON.stringify(response));
    }
  }
}
