import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProductImageService, ProductImage } from '../product-image.service';
import { ProductService, Product } from '../product.service';

@Component({
  selector: 'app-product-images',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './product-images.component.html',
  styleUrl: './product-images.component.css'
})
export class ProductImagesComponent implements OnInit {

  productId!: number;
  product: Product | null = null;
  images: ProductImage[] = [];

  newImageUrl = '';
  newIsPrimary = false;

  isLoading = true;
  errorMessage = '';
  successMessage = '';

  constructor(
    private imageService: ProductImageService,
    private productService: ProductService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadProduct();
    this.loadImages();
  }

  loadProduct(): void {
    this.productService.getById(this.productId).subscribe({
      next: (data) => this.product = data
    });
  }

  loadImages(): void {
    this.isLoading = true;
    this.imageService.getImages(this.productId).subscribe({
      next: (data) => {
        this.images = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load images';
        this.isLoading = false;
      }
    });
  }

  onAddImage(): void {
    if (!this.newImageUrl.trim()) {
      this.errorMessage = 'Enter an image URL';
      return;
    }

    this.errorMessage = '';
    this.successMessage = '';

    this.imageService.addImage(this.productId, {
      imageUrl: this.newImageUrl,
      isPrimary: this.newIsPrimary,
      displayOrder: this.images.length
    }).subscribe({
      next: () => {
        this.successMessage = 'Image added';
        this.newImageUrl = '';
        this.newIsPrimary = false;
        this.loadImages();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to add image';
      }
    });
  }

  onSetPrimary(image: ProductImage): void {
    this.imageService.setPrimary(this.productId, image.id).subscribe({
      next: () => {
        this.loadImages();
      },
      error: () => {
        this.errorMessage = 'Failed to set primary image';
      }
    });
  }

  onDeleteImage(image: ProductImage): void {
    if (!confirm('Delete this image?')) {
      return;
    }

    this.imageService.deleteImage(this.productId, image.id).subscribe({
      next: () => {
        this.loadImages();
      },
      error: () => {
        this.errorMessage = 'Failed to delete image';
      }
    });
  }
}
