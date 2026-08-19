import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CheckoutService, OrderResponse } from '../../checkout/checkout.service';

@Component({
  selector: 'app-order-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './order-list.component.html',
  styleUrl: './order-list.component.css'
})
export class OrderListComponent implements OnInit {

  orders: OrderResponse[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(private checkoutService: CheckoutService) { }

  ngOnInit(): void {
    this.checkoutService.getMyOrders().subscribe({
      next: (data) => {
        this.orders = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load orders';
        this.isLoading = false;
      }
    });
  }
}
