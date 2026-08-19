import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface ShippingRule {
  id: number;
  name: string;
  regionPattern: string;
  minOrderFreeShipping: number | null;
  baseCharge: number;
  isActive: boolean;
}

export interface ShippingRuleRequest {
  name: string;
  regionPattern: string;
  minOrderFreeShipping: number | null;
  baseCharge: number;
}

export interface TaxConfig {
  id: number;
  name: string;
  taxPercent: number;
  isActive: boolean;
}

export interface TaxConfigRequest {
  name: string;
  taxPercent: number;
}

@Injectable({
  providedIn: 'root'
})
export class ShippingTaxService {

  private readonly shippingUrl = `${environment.apiUrl}/admin/shipping-rules`;
  private readonly taxUrl = `${environment.apiUrl}/admin/tax-configs`;
  constructor(private http: HttpClient) { }

  getAllShippingRules(): Observable<ShippingRule[]> {
    return this.http.get<ShippingRule[]>(this.shippingUrl);
  }

  createShippingRule(request: ShippingRuleRequest): Observable<ShippingRule> {
    return this.http.post<ShippingRule>(this.shippingUrl, request);
  }

  getAllTaxConfigs(): Observable<TaxConfig[]> {
    return this.http.get<TaxConfig[]>(this.taxUrl);
  }

  createTaxConfig(request: TaxConfigRequest): Observable<TaxConfig> {
    return this.http.post<TaxConfig>(this.taxUrl, request);
  }
}
