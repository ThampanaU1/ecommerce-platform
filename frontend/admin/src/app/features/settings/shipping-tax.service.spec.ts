import { TestBed } from '@angular/core/testing';

import { ShippingTaxService } from './shipping-tax.service';

describe('ShippingTaxService', () => {
  let service: ShippingTaxService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShippingTaxService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
