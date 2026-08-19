import { Routes } from '@angular/router';
import { LoginComponent } from './core/auth/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { CategoryListComponent } from './features/categories/category-list/category-list.component';
import { CategoryFormComponent } from './features/categories/category-form/category-form.component';
import { ProductListComponent } from './features/products/product-list/product-list.component';
import { ProductFormComponent } from './features/products/product-form/product-form.component';
import { ProductPricingComponent } from './features/products/product-pricing/product-pricing.component';
import { ProductInventoryComponent } from './features/products/product-inventory/product-inventory.component';
import { ProductImagesComponent } from './features/products/product-images/product-images.component';
import { OrderListComponent } from './features/orders/order-list/order-list.component';
import { CouponListComponent } from './features/coupons/coupon-list/coupon-list.component';
import { SettingsPageComponent } from './features/settings/settings-page/settings-page.component';
import { BannerListComponent } from './features/banners/banner-list/banner-list.component';
import { authGuard } from './core/auth/auth.guard';
import { ReviewListComponent } from './features/reviews/review-list/review-list.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'categories', component: CategoryListComponent, canActivate: [authGuard] },
  { path: 'categories/new', component: CategoryFormComponent, canActivate: [authGuard] },
  { path: 'categories/:id/edit', component: CategoryFormComponent, canActivate: [authGuard] },
  { path: 'products', component: ProductListComponent, canActivate: [authGuard] },
  { path: 'products/new', component: ProductFormComponent, canActivate: [authGuard] },
  { path: 'products/:id/edit', component: ProductFormComponent, canActivate: [authGuard] },
  { path: 'products/:id/pricing', component: ProductPricingComponent, canActivate: [authGuard] },
  { path: 'products/:id/inventory', component: ProductInventoryComponent, canActivate: [authGuard] },
  { path: 'products/:id/images', component: ProductImagesComponent, canActivate: [authGuard] },
  { path: 'orders', component: OrderListComponent, canActivate: [authGuard] },
  { path: 'coupons', component: CouponListComponent, canActivate: [authGuard] },
  { path: 'settings', component: SettingsPageComponent, canActivate: [authGuard] },
  { path: 'banners', component: BannerListComponent, canActivate: [authGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'reviews', component: ReviewListComponent, canActivate: [authGuard] }
];
