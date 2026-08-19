import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService, ProductRequest } from '../product.service';
import { CategoryService, Category } from '../../categories/category.service';

@Component({
  selector: 'app-product-form',
  imports: [CommonModule, FormsModule],
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.css'
})
export class ProductFormComponent implements OnInit {

  isEditMode = false;
  productId: number | null = null;

  categories: Category[] = [];

  formData: ProductRequest = {
    sku: '',
    name: '',
    description: '',
    categoryId: 0,
    featured: false
  };

  isLoading = false;
  errorMessage = '';

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadCategories();

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditMode = true;
      this.productId = Number(idParam);
      this.loadProduct(this.productId);
    }
  }

  loadCategories(): void {
    this.categoryService.getAll().subscribe({
      next: (data) => {
        this.categories = data;
      },
      error: () => {
        this.errorMessage = 'Failed to load categories';
      }
    });
  }

  loadProduct(id: number): void {
    this.isLoading = true;
    this.productService.getById(id).subscribe({
      next: (product) => {
        this.formData = {
          sku: product.sku,
          name: product.name,
          description: product.description,
          categoryId: product.categoryId,
          featured: product.featured
        };
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load product';
        this.isLoading = false;
      }
    });
  }

  onSubmit(): void {
    this.isLoading = true;
    this.errorMessage = '';

    const request$ = this.isEditMode && this.productId
      ? this.productService.update(this.productId, this.formData)
      : this.productService.create(this.formData);

    request$.subscribe({
      next: () => {
        this.router.navigate(['/products']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Failed to save product';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/products']);
  }
}
