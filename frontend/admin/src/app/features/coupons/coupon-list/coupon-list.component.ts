import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CouponService, Coupon, CouponRequest } from '../coupon.service';

@Component({
  selector: 'app-coupon-list',
  imports: [CommonModule, FormsModule],
  templateUrl: './coupon-list.component.html',
  styleUrl: './coupon-list.component.css'
})
export class CouponListComponent implements OnInit {

  coupons: Coupon[] = [];
  isLoading = true;
  errorMessage = '';
  successMessage = '';

  showForm = false;

  formData: CouponRequest = {
    code: '',
    type: 'PERCENT',
    value: 0,
    minOrderValue: 0,
    maxDiscount: null,
    usageLimitTotal: null,
    usageLimitPerUser: null,
    startDate: '',
    endDate: ''
  };

  constructor(private couponService: CouponService) { }

  ngOnInit(): void {
    this.loadCoupons();
  }

  loadCoupons(): void {
    this.isLoading = true;
    this.couponService.getAll().subscribe({
      next: (data) => {
        this.coupons = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load coupons';
        this.isLoading = false;
      }
    });
  }

  onCreate(): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.couponService.create(this.formData).subscribe({
      next: () => {
        this.successMessage = 'Coupon created';
        this.showForm = false;
        this.resetForm();
        this.loadCoupons();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to create coupon';
      }
    });
  }

  onToggleStatus(coupon: Coupon): void {
    this.couponService.setStatus(coupon.id, !coupon.isActive).subscribe({
      next: (updated) => {
        coupon.isActive = updated.isActive;
      },
      error: () => {
        this.errorMessage = 'Failed to update coupon status';
      }
    });
  }

  resetForm(): void {
    this.formData = {
      code: '',
      type: 'PERCENT',
      value: 0,
      minOrderValue: 0,
      maxDiscount: null,
      usageLimitTotal: null,
      usageLimitPerUser: null,
      startDate: '',
      endDate: ''
    };
  }
}
