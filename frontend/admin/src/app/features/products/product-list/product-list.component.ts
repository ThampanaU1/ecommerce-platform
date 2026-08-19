import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProductService, Product } from '../product.service';

@Component({
  selector: 'app-product-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit {

  products: Product[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.isLoading = true;
    this.productService.getAll().subscribe({
      next: (data) => {
        this.products = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load products';
        this.isLoading = false;
      }
    });
  }

  toggleStatus(product: Product): void {
    const newStatus = product.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    this.productService.setStatus(product.id, newStatus).subscribe({
      next: (updated) => {
        product.status = updated.status;
      },
      error: () => {
        this.errorMessage = 'Failed to update status';
      }
    });
  }

  deleteProduct(product: Product): void {
    if (!confirm(`Delete product "${product.name}"? This cannot be undone.`)) {
      return;
    }

    this.productService.delete(product.id).subscribe({
      next: () => {
        this.products = this.products.filter(p => p.id !== product.id);
      },
      error: () => {
        this.errorMessage = 'Failed to delete product';
      }
    });
  }
}
