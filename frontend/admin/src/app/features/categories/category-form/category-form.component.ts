import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryService, CategoryRequest } from '../category.service';

@Component({
  selector: 'app-category-form',
  imports: [CommonModule, FormsModule],
  templateUrl: './category-form.component.html',
  styleUrl: './category-form.component.css'
})
export class CategoryFormComponent implements OnInit {

  isEditMode = false;
  categoryId: number | null = null;

  formData: CategoryRequest = {
    name: '',
    description: '',
    imageUrl: '',
    displayOrder: 0
  };

  isLoading = false;
  errorMessage = '';

  constructor(
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      this.isEditMode = true;
      this.categoryId = Number(idParam);
      this.loadCategory(this.categoryId);
    }
  }

  loadCategory(id: number): void {
    this.isLoading = true;
    this.categoryService.getById(id).subscribe({
      next: (category) => {
        this.formData = {
          name: category.name,
          description: category.description,
          imageUrl: category.imageUrl,
          displayOrder: category.displayOrder
        };
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load category';
        this.isLoading = false;
      }
    });
  }

  onSubmit(): void {
    this.isLoading = true;
    this.errorMessage = '';

    const request$ = this.isEditMode && this.categoryId
      ? this.categoryService.update(this.categoryId, this.formData)
      : this.categoryService.create(this.formData);

    request$.subscribe({
      next: () => {
        this.router.navigate(['/categories']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Failed to save category';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/categories']);
  }
}
