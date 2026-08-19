import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService, AdminOrder } from '../order.service';

@Component({
  selector: 'app-order-list',
  imports: [CommonModule],
  templateUrl: './order-list.component.html',
  styleUrl: './order-list.component.css'
})
export class OrderListComponent implements OnInit {

  orders: AdminOrder[] = [];
  isLoading = true;
  errorMessage = '';

  statusOptions = ['PENDING', 'CONFIRMED', 'PACKED', 'SHIPPED', 'DELIVERED', 'CANCELLED'];

  constructor(private orderService: OrderService) { }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.isLoading = true;
    this.orderService.getAll().subscribe({
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

  onStatusChange(order: AdminOrder, newStatus: string): void {
    this.orderService.updateStatus(order.id, newStatus).subscribe({
      next: (updated) => {
        order.status = updated.status;
      },
      error: () => {
        this.errorMessage = 'Failed to update order status';
      }
    });
  }
}
