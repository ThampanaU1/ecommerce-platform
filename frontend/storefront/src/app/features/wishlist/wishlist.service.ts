import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

export interface WishlistItem {
  id: number;
  productId: number;
  productName: string;
  productSku: string;
  sellingPrice: number | null;
  primaryImageUrl: string | null;
  inStock: boolean;
  addedAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class WishlistService {

  private readonly apiUrl = 'http://localhost:8081/api/v1/wishlist';

  private wishlistProductIds = new Set<number>();
  private wishlistSubject = new BehaviorSubject<Set<number>>(new Set());
  wishlistIds$ = this.wishlistSubject.asObservable();

  constructor(private http: HttpClient) { }

  getMyWishlist(): Observable<WishlistItem[]> {
    return this.http.get<WishlistItem[]>(this.apiUrl).pipe(
      tap(items => {
        this.wishlistProductIds = new Set(items.map(i => i.productId));
        this.wishlistSubject.next(this.wishlistProductIds);
      })
    );
  }

  addToWishlist(productId: number): Observable<WishlistItem> {
    return this.http.post<WishlistItem>(`${this.apiUrl}/${productId}`, {}).pipe(
      tap(() => {
        this.wishlistProductIds.add(productId);
        this.wishlistSubject.next(this.wishlistProductIds);
      })
    );
  }

  removeFromWishlist(productId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${productId}`).pipe(
      tap(() => {
        this.wishlistProductIds.delete(productId);
        this.wishlistSubject.next(this.wishlistProductIds);
      })
    );
  }

  isInWishlist(productId: number): boolean {
    return this.wishlistProductIds.has(productId);
  }
}
