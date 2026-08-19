import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home/home.component';
import { ProductDetailComponent } from './features/products/product-detail/product-detail.component';
import { CartPageComponent } from './features/cart/cart-page/cart-page.component';
import { RegisterComponent } from './core/auth/register/register.component';
import { LoginComponent } from './core/auth/login/login.component';
import { CheckoutPageComponent } from './features/checkout/checkout-page/checkout-page.component';
import { OrderListComponent } from './features/orders/order-list/order-list.component';
import { ProfilePageComponent } from './features/profile/profile-page/profile-page.component';
import { WishlistPageComponent } from './features/wishlist/wishlist-page/wishlist-page.component';
import { authGuard } from './core/auth/auth.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'products/:id', component: ProductDetailComponent },
  { path: 'cart', component: CartPageComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'checkout', component: CheckoutPageComponent, canActivate: [authGuard] },
  { path: 'orders', component: OrderListComponent, canActivate: [authGuard] },
  { path: 'profile', component: ProfilePageComponent, canActivate: [authGuard] },
  { path: 'wishlist', component: WishlistPageComponent, canActivate: [authGuard] }
];
