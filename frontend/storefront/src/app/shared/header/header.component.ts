import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CartService } from '../../features/cart/cart.service';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  selector: 'app-header',
  imports: [CommonModule, RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {

  itemCount = 0;

  constructor(
    private cartService: CartService,
    public authService: AuthService
  ) { }

  ngOnInit(): void {
    this.cartService.cart$.subscribe(items => {
      this.itemCount = items.reduce((sum, item) => sum + item.quantity, 0);
    });
  }

  onLogout(): void {
    this.authService.logout();
  }
}
