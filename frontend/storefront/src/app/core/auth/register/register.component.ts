import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  name = '';
  email = '';
  password = '';
  errorMessage = '';
  isLoading = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  onSubmit(): void {
    this.errorMessage = '';
    this.isLoading = true;

    this.authService.register({ name: this.name, email: this.email, password: this.password }).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Registration failed. Please try again.';
      }
    });
  }
}
