import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CategoryService, Category } from '../category.service';

@Component({
  selector: 'app-category-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './category-list.component.html',
  styleUrl: './category-list.component.css'
})
export class CategoryListComponent implements OnInit {
  // ... rest stays exactly the same, no other changes

  categories: Category[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(private categoryService: CategoryService) { }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.isLoading = true;
    this.categoryService.getAll().subscribe({
      next: (data) => {
        this.categories = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load categories';
        this.isLoading = false;
      }
    });
  }

  toggleStatus(category: Category): void {
    this.categoryService.setStatus(category.id, !category.active).subscribe({
      next: (updated) => {
        category.active = updated.active;
      },
      error: () => {
        this.errorMessage = 'Failed to update status';
      }
    });
  }

  deleteCategory(category: Category): void {
    if (!confirm(`Delete category "${category.name}"? This cannot be undone.`)) {
      return;
    }

    this.categoryService.delete(category.id).subscribe({
      next: () => {
        this.categories = this.categories.filter(c => c.id !== category.id);
      },
      error: () => {
        this.errorMessage = 'Failed to delete category';
      }
    });
  }
}
