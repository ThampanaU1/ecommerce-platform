import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BannerService, Banner, BannerRequest } from '../banner.service';

@Component({
  selector: 'app-banner-list',
  imports: [CommonModule, FormsModule],
  templateUrl: './banner-list.component.html',
  styleUrl: './banner-list.component.css'
})
export class BannerListComponent implements OnInit {

  banners: Banner[] = [];
  isLoading = true;
  errorMessage = '';
  successMessage = '';
  showForm = false;

  formData: BannerRequest = {
    title: '',
    imageUrl: '',
    linkUrl: '/',
    displayOrder: 0
  };

  constructor(private bannerService: BannerService) { }

  ngOnInit(): void {
    this.loadBanners();
  }

  loadBanners(): void {
    this.isLoading = true;
    this.bannerService.getAll().subscribe({
      next: (data) => {
        this.banners = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load banners';
        this.isLoading = false;
      }
    });
  }

  onCreate(): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.bannerService.create(this.formData).subscribe({
      next: () => {
        this.successMessage = 'Banner created';
        this.showForm = false;
        this.formData = { title: '', imageUrl: '', linkUrl: '/', displayOrder: 0 };
        this.loadBanners();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to create banner';
      }
    });
  }

  onToggleStatus(banner: Banner): void {
    this.bannerService.setStatus(banner.id, !banner.isActive).subscribe({
      next: (updated) => banner.isActive = updated.isActive
    });
  }

  onDelete(banner: Banner): void {
    if (!confirm('Delete this banner?')) return;

    this.bannerService.delete(banner.id).subscribe({
      next: () => this.banners = this.banners.filter(b => b.id !== banner.id)
    });
  }
}
