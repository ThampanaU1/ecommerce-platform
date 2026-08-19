import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { BehaviorSubject } from 'rxjs';
import { Product } from '../products/product.service';

export interface CartItem {
  product: Product;
  quantity: number;
}

const CART_STORAGE_KEY = 'cart';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private cartItems: CartItem[] = [];
  private cartSubject = new BehaviorSubject<CartItem[]>([]);
  private isBrowser: boolean;

  cart$ = this.cartSubject.asObservable();

  constructor(@Inject(PLATFORM_ID) platformId: Object) {
    this.isBrowser = isPlatformBrowser(platformId);
    this.loadFromStorage();
  }

  private loadFromStorage(): void {
    if (!this.isBrowser) {
      return;
    }
    const stored = localStorage.getItem(CART_STORAGE_KEY);
    if (stored) {
      this.cartItems = JSON.parse(stored);
      this.cartSubject.next(this.cartItems);
    }
  }

  private saveToStorage(): void {
    if (this.isBrowser) {
      localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(this.cartItems));
    }
    this.cartSubject.next([...this.cartItems]);
  }

  addToCart(product: Product, quantity: number = 1): void {
    const existing = this.cartItems.find(item => item.product.id === product.id);

    if (existing) {
      existing.quantity += quantity;
    } else {
      this.cartItems.push({ product, quantity });
    }

    this.saveToStorage();
  }

  updateQuantity(productId: number, quantity: number): void {
    const item = this.cartItems.find(i => i.product.id === productId);
    if (item) {
      if (quantity <= 0) {
        this.removeItem(productId);
      } else {
        item.quantity = quantity;
        this.saveToStorage();
      }
    }
  }

  removeItem(productId: number): void {
    this.cartItems = this.cartItems.filter(item => item.product.id !== productId);
    this.saveToStorage();
  }

  clearCart(): void {
    this.cartItems = [];
    this.saveToStorage();
  }

  getItemCount(): number {
    return this.cartItems.reduce((sum, item) => sum + item.quantity, 0);
  }

  getTotal(): number {
    return this.cartItems.reduce((sum, item) => sum + (item.product.sellingPrice || 0) * item.quantity, 0);
  }

  getItems(): CartItem[] {
    return [...this.cartItems];
  }
}
