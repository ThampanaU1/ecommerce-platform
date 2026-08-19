import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CartService, CartItem } from '../../cart/cart.service';
import { CheckoutService, AddressRequest } from '../checkout.service';

@Component({
  selector: 'app-checkout-page',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './checkout-page.component.html',
  styleUrl: './checkout-page.component.css'
})
export class CheckoutPageComponent implements OnInit {

  items: CartItem[] = [];
  subtotal = 0;

  address: AddressRequest = {
    label: 'Home',
    line1: '',
    line2: '',
    city: '',
    state: '',
    pincode: '',
    country: 'India'
  };

  couponCode = '';
  appliedDiscount = 0;
  couponMessage = '';
  couponError = '';
  isValidatingCoupon = false;

  isLoading = false;
  errorMessage = '';
  orderPlaced = false;
  orderNumber = '';

  constructor(
    private cartService: CartService,
    private checkoutService: CheckoutService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cartService.cart$.subscribe(items => {
      this.items = items;
      this.subtotal = this.cartService.getTotal();
    });
  }

  get estimatedTotal(): number {
    return Math.max(0, this.subtotal - this.appliedDiscount);
  }

  onApplyCoupon(): void {
    if (!this.couponCode.trim()) {
      return;
    }

    this.isValidatingCoupon = true;
    this.couponError = '';
    this.couponMessage = '';

    this.checkoutService.validateCoupon(this.couponCode.trim(), this.subtotal).subscribe({
      next: (result) => {
        this.appliedDiscount = result.discount;
        this.couponMessage = `Coupon applied! You saved ₹${result.discount}`;
        this.isValidatingCoupon = false;
      },
      error: (err) => {
        this.appliedDiscount = 0;
        this.couponError = err.error?.message || 'Invalid coupon code';
        this.isValidatingCoupon = false;
      }
    });
  }

  onRemoveCoupon(): void {
    this.couponCode = '';
    this.appliedDiscount = 0;
    this.couponMessage = '';
    this.couponError = '';
  }

  onPlaceOrder(): void {
    if (this.items.length === 0) {
      this.errorMessage = 'Your cart is empty';
      return;
    }

    this.errorMessage = '';
    this.isLoading = true;

    const request = {
      shippingAddress: this.address,
      items: this.items.map(item => ({
        productId: item.product.id,
        quantity: item.quantity
      })),
      couponCode: this.appliedDiscount > 0 ? this.couponCode.trim() : null
    };

    this.checkoutService.placeOrder(request).subscribe({
      next: (order) => {
        this.isLoading = false;
        this.orderPlaced = true;
        this.orderNumber = order.orderNumber;
        this.cartService.clearCart();
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Failed to place order. Please try again.';
      }
    });
  }
}
