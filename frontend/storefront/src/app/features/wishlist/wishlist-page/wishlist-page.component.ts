import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { WishlistService, WishlistItem } from '../wishlist.service';
import { CartService } from '../../cart/cart.service';
import { ProductService } from '../../products/product.service';

@Component({
  selector: 'app-wishlist-page',
  imports: [CommonModule, RouterLink],
  templateUrl: './wishlist-page.component.html',
  styleUrl: './wishlist-page.component.css'
})
export class WishlistPageComponent implements OnInit {

  items: WishlistItem[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(
    private wishlistService: WishlistService,
    private cartService: CartService,
    private productService: ProductService
  ) { }

  ngOnInit(): void {
    this.loadWishlist();
  }

  loadWishlist(): void {
    this.isLoading = true;
    this.wishlistService.getMyWishlist().subscribe({
      next: (data) => {
        this.items = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load wishlist';
        this.isLoading = false;
      }
    });
  }

  onRemove(item: WishlistItem): void {
    this.wishlistService.removeFromWishlist(item.productId).subscribe({
      next: () => {
        this.items = this.items.filter(i => i.productId !== item.productId);
      }
    });
  }

  onAddToCart(item: WishlistItem): void {
    this.productService.getProductById(item.productId).subscribe({
      next: (product) => {
        this.cartService.addToCart(product, 1);
      }
    });
  }
}
