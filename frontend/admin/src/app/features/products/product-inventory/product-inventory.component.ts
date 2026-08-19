import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProductInventoryService, Inventory, SetInventoryRequest, AdjustStockRequest } from '../product-inventory.service';
import { ProductService, Product } from '../product.service';

@Component({
  selector: 'app-product-inventory',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './product-inventory.component.html',
  styleUrl: './product-inventory.component.css'
})
export class ProductInventoryComponent implements OnInit {

  productId!: number;
  product: Product | null = null;
  inventory: Inventory | null = null;

  setFormData: SetInventoryRequest = {
    availableQuantity: 0,
    reorderLevel: 0
  };

  adjustAmount: number = 0;

  isLoading = true;
  errorMessage = '';
  successMessage = '';

  constructor(
    private inventoryService: ProductInventoryService,
    private productService: ProductService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadProduct();
    this.loadInventory();
  }

  loadProduct(): void {
    this.productService.getById(this.productId).subscribe({
      next: (data) => this.product = data
    });
  }

  loadInventory(): void {
    this.inventoryService.getInventory(this.productId).subscribe({
      next: (data) => {
        this.inventory = data;
        this.isLoading = false;
      },
      error: () => {
        this.inventory = null;
        this.isLoading = false;
      }
    });
  }

  onSetInventory(): void {
    this.clearMessages();
    this.inventoryService.setInventory(this.productId, this.setFormData).subscribe({
      next: (data) => {
        this.inventory = data;
        this.successMessage = 'Inventory set successfully';
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to set inventory';
      }
    });
  }

  onAdjustStock(): void {
    this.clearMessages();

    if (!this.adjustAmount) {
      this.errorMessage = 'Enter a quantity to adjust';
      return;
    }

    this.inventoryService.adjustStock(this.productId, { quantityChange: this.adjustAmount }).subscribe({
      next: (data) => {
        this.inventory = data;
        this.successMessage = `Stock adjusted by ${this.adjustAmount > 0 ? '+' : ''}${this.adjustAmount}`;
        this.adjustAmount = 0;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to adjust stock';
      }
    });
  }

  private clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }
}
