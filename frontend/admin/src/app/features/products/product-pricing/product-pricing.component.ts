import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProductPricingService, ProductPrice, SetPriceRequest } from '../product-pricing.service';
import { ProductService, Product } from '../product.service';

@Component({
  selector: 'app-product-pricing',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './product-pricing.component.html',
  styleUrl: './product-pricing.component.css'
})
export class ProductPricingComponent implements OnInit {

  productId!: number;
  product: Product | null = null;
  currentPrice: ProductPrice | null = null;
  priceHistory: ProductPrice[] = [];

  formData: SetPriceRequest = {
    mrp: 0,
    sellingPrice: 0
  };

  isLoading = true;
  errorMessage = '';
  successMessage = '';

  constructor(
    private pricingService: ProductPricingService,
    private productService: ProductService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadProduct();
    this.loadCurrentPrice();
    this.loadHistory();
  }

  loadProduct(): void {
    this.productService.getById(this.productId).subscribe({
      next: (data) => this.product = data
    });
  }

  loadCurrentPrice(): void {
    this.pricingService.getCurrentPrice(this.productId).subscribe({
      next: (data) => {
        this.currentPrice = data;
        this.isLoading = false;
      },
      error: () => {
        this.currentPrice = null;
        this.isLoading = false;
      }
    });
  }

  loadHistory(): void {
    this.pricingService.getPriceHistory(this.productId).subscribe({
      next: (data) => this.priceHistory = data
    });
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.pricingService.setPrice(this.productId, this.formData).subscribe({
      next: () => {
        this.successMessage = 'Price updated successfully';
        this.formData = { mrp: 0, sellingPrice: 0 };
        this.loadCurrentPrice();
        this.loadHistory();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to set price';
      }
    });
  }
}
