import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-profile-page',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css'
})
export class ProfilePageComponent implements OnInit {

  name = '';
  email = '';
  roles: string[] = [];

  isLoading = true;
  isSaving = false;
  errorMessage = '';
  successMessage = '';

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.isLoading = true;
    this.authService.getProfile().subscribe({
      next: (data) => {
        this.name = data.name;
        this.email = data.email;
        this.roles = data.roles;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load profile';
        this.isLoading = false;
      }
    });
  }

  onSave(): void {
    this.errorMessage = '';
    this.successMessage = '';
    this.isSaving = true;

    this.authService.updateProfile(this.name).subscribe({
      next: () => {
        this.successMessage = 'Profile updated successfully';
        this.isSaving = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to update profile';
        this.isSaving = false;
      }
    });
  }
}
