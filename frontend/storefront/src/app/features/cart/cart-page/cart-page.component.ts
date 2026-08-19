import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CartService, CartItem } from '../cart.service';

@Component({
  selector: 'app-cart-page',
  imports: [CommonModule, RouterLink],
  templateUrl: './cart-page.component.html',
  styleUrl: './cart-page.component.css'
})
export class CartPageComponent implements OnInit {

  items: CartItem[] = [];
  total = 0;

  constructor(private cartService: CartService) { }

  ngOnInit(): void {
    this.cartService.cart$.subscribe(items => {
      this.items = items;
      this.total = this.cartService.getTotal();
    });
  }

  increaseQuantity(item: CartItem): void {
    this.cartService.updateQuantity(item.product.id, item.quantity + 1);
  }

  decreaseQuantity(item: CartItem): void {
    this.cartService.updateQuantity(item.product.id, item.quantity - 1);
  }

  removeItem(item: CartItem): void {
    this.cartService.removeItem(item.product.id);
  }
}
