import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { CartService } from '../../cart/cart.service';
import { WishlistService } from '../../wishlist/wishlist.service';
import { AuthService } from '../../../core/auth/auth.service';
import { ProductService, Product, Review, ReviewRequest } from '../product.service';
@Component({
  selector: 'app-product-detail',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css'
})
export class ProductDetailComponent implements OnInit {

  images: string[] = [];
  selectedImage = '';
  reviews: Review[] = [];
  newRating = 5;
  newComment = '';
  reviewSubmitted = false;
  reviewError = '';
  product: Product | null = null;
  isLoading = true;
  errorMessage = '';
  addedToCart = false;
  isInWishlist = false;
  wishlistMessage = '';

  constructor(
    private productService: ProductService,
    private cartService: CartService,
    private wishlistService: WishlistService,
    private authService: AuthService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadProduct(id);

    if (this.authService.isLoggedIn()) {
      this.wishlistService.getMyWishlist().subscribe({
        next: () => this.isInWishlist = this.wishlistService.isInWishlist(id),
      });
    }
  }

  loadImages(id: number): void {
    this.productService.getProductImages(id).subscribe({
      next: (data) => {
        this.images = data;
        this.selectedImage = data.length > 0 ? data[0] : (this.product?.primaryImageUrl || '');
      }
    });
  }

  selectImage(url: string): void {
    this.selectedImage = url;
  }



  loadProduct(id: number): void {
    this.isLoading = true;
    this.loadReviews(id);
    this.productService.getProductById(id).subscribe({
      next: (data) => {
        this.product = data;
        this.loadImages(id);
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Product not found';
        this.isLoading = false;
      }
    });
  }
  loadReviews(id: number): void {
    this.productService.getReviews(id).subscribe({
      next: (data) => this.reviews = data
    });
  }

  onSubmitReview(): void {
    if (!this.product) return;

    this.reviewError = '';

    const request: ReviewRequest = { rating: this.newRating, comment: this.newComment };

    this.productService.submitReview(this.product.id, request).subscribe({
      next: () => {
        this.reviewSubmitted = true;
        this.newComment = '';
      },
      error: (err) => {
        this.reviewError = err.error?.message || 'Failed to submit review';
      }
    });
  }

  onAddToCart(): void {
    if (this.product) {
      this.cartService.addToCart(this.product, 1);
      this.addedToCart = true;
      setTimeout(() => this.addedToCart = false, 2000);
    }
  }
  getStars(rating: number): string {
    return '★'.repeat(rating) + '☆'.repeat(5 - rating);
  }
  onToggleWishlist(): void {
    if (!this.authService.isLoggedIn()) {
      this.wishlistMessage = 'Please log in to use wishlist';
      return;
    }

    if (!this.product) return;

    if (this.isInWishlist) {
      this.wishlistService.removeFromWishlist(this.product.id).subscribe({
        next: () => this.isInWishlist = false
      });
    } else {
      this.wishlistService.addToWishlist(this.product.id).subscribe({
        next: () => this.isInWishlist = true
      });
    }
  }
}
