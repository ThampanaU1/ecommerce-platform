import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ShippingTaxService, ShippingRule, TaxConfig } from '../shipping-tax.service';

@Component({
  selector: 'app-settings-page',
  imports: [CommonModule, FormsModule],
  templateUrl: './settings-page.component.html',
  styleUrl: './settings-page.component.css'
})
export class SettingsPageComponent implements OnInit {

  shippingRules: ShippingRule[] = [];
  taxConfigs: TaxConfig[] = [];

  shippingForm = {
    name: '',
    regionPattern: '*',
    minOrderFreeShipping: null as number | null,
    baseCharge: 0
  };

  taxForm = {
    name: '',
    taxPercent: 0
  };

  errorMessage = '';
  successMessage = '';

  constructor(private shippingTaxService: ShippingTaxService) { }

  ngOnInit(): void {
    this.loadShippingRules();
    this.loadTaxConfigs();
  }

  loadShippingRules(): void {
    this.shippingTaxService.getAllShippingRules().subscribe({
      next: (data) => this.shippingRules = data,
      error: () => this.errorMessage = 'Failed to load shipping rules'
    });
  }

  loadTaxConfigs(): void {
    this.shippingTaxService.getAllTaxConfigs().subscribe({
      next: (data) => this.taxConfigs = data,
      error: () => this.errorMessage = 'Failed to load tax configs'
    });
  }

  onCreateShippingRule(): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.shippingTaxService.createShippingRule(this.shippingForm).subscribe({
      next: () => {
        this.successMessage = 'Shipping rule updated';
        this.loadShippingRules();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to update shipping rule';
      }
    });
  }

  onCreateTaxConfig(): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.shippingTaxService.createTaxConfig(this.taxForm).subscribe({
      next: () => {
        this.successMessage = 'Tax config updated';
        this.loadTaxConfigs();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to update tax config';
      }
    });
  }
}
