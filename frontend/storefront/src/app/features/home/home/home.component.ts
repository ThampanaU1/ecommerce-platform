import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { ProductService, Product, Category, Banner } from '../../products/product.service';

@Component({
  selector: 'app-home',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  banners: Banner[] = [];
  allProducts: Product[] = [];
  products: Product[] = [];
  categories: Category[] = [];
  isLoading = true;
  errorMessage = '';

  searchTerm = '';
  selectedCategoryId: number | null = null;
  sortOption = 'default';

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.loadCategories();
    this.loadProducts();
    this.loadBanners();
  }

  loadCategories(): void {
    this.productService.getCategories().subscribe({
      next: (data) => this.categories = data,
      error: () => this.errorMessage = 'Failed to load categories'
    });
  }
  loadBanners(): void {
    this.productService.getBanners().subscribe({
      next: (data) => this.banners = data
    });
  }

  loadProducts(): void {
    this.isLoading = true;
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.allProducts = data;
        this.applyFilters();
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load products';
        this.isLoading = false;
      }
    });
  }


  filterByCategory(categoryId: number | null): void {
    this.selectedCategoryId = categoryId;
    this.applyFilters();
  }

  applyFilters(): void {
    let result = [...this.allProducts];

    if (this.selectedCategoryId !== null) {
      result = result.filter(p => p.categoryId === this.selectedCategoryId);
    }

    if (this.searchTerm.trim()) {
      const term = this.searchTerm.trim().toLowerCase();
      result = result.filter(p => p.name.toLowerCase().includes(term));
    }

    if (this.sortOption === 'price-low') {
      result.sort((a, b) => (a.sellingPrice || 0) - (b.sellingPrice || 0));
    } else if (this.sortOption === 'price-high') {
      result.sort((a, b) => (b.sellingPrice || 0) - (a.sellingPrice || 0));
    } else if (this.sortOption === 'name') {
      result.sort((a, b) => a.name.localeCompare(b.name));
    }

    this.products = result;
  }
}
