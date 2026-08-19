import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReviewService, Review } from '../review.service';

@Component({
  selector: 'app-review-list',
  imports: [CommonModule],
  templateUrl: './review-list.component.html',
  styleUrl: './review-list.component.css'
})
export class ReviewListComponent implements OnInit {

  reviews: Review[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(private reviewService: ReviewService) { }

  ngOnInit(): void {
    this.loadReviews();
  }

  loadReviews(): void {
    this.isLoading = true;
    this.reviewService.getAll().subscribe({
      next: (data) => {
        this.reviews = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load reviews';
        this.isLoading = false;
      }
    });
  }

  onApprove(review: Review): void {
    this.reviewService.updateStatus(review.id, 'APPROVED').subscribe({
      next: (updated) => review.status = updated.status
    });
  }

  onReject(review: Review): void {
    this.reviewService.updateStatus(review.id, 'REJECTED').subscribe({
      next: (updated) => review.status = updated.status
    });
  }

  getStars(rating: number): string {
    return '★'.repeat(rating) + '☆'.repeat(5 - rating);
  }
}
