# E-Commerce Platform

A full-stack e-commerce application with a Spring Boot backend, Angular Admin Panel, and Angular Storefront.

## Project Structure
## Tech Stack

- **Backend:** Java 21, Spring Boot 4.1, Spring Security, Spring Data JPA, MySQL 8, Flyway
- **Frontend:** Angular 19 (Admin: client-side, Storefront: SSR-enabled)
- **Auth:** JWT-based authentication with role-based authorization

## Prerequisites

- Java 21+
- Node.js 18+ and Angular CLI
- MySQL 8.x running locally

## Setup

### 1. Database

```sql
CREATE DATABASE ecommerce_db CHARACTER SET utf8mb4;
CREATE USER 'ecommerce_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON ecommerce_db.* TO 'ecommerce_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Backend
Runs on `http://localhost:8081`. Database schema is created automatically via Flyway migrations on first startup.

### 3. Admin Panel
Runs on `http://localhost:4200`.

### 4. Storefront
Runs on `http://localhost:4300`.

## Creating an Admin User

New registrations default to the `CUSTOMER` role. To promote a user to `ADMIN`:

```sql
INSERT INTO user_roles (user_id, role_id)
SELECT id, (SELECT id FROM roles WHERE name = 'ADMIN')
FROM users WHERE email = 'your-email@example.com';
```

## Features

- **Catalog:** Categories, products, pricing with history, inventory, product images
- **Commerce:** Cart, checkout, coupons/discounts, order management
- **Configuration:** Admin-editable shipping rules and tax rates (no hardcoding, no redeploy needed)
- **Customer:** Registration/login, profile, wishlist, order history, product reviews
- **Content:** Homepage banners

## Environment Variables (Production)

| Variable | Description |
|---|---|
| `DB_HOST`, `DB_PORT`, `DB_NAME` | Database connection details |
| `DB_USERNAME`, `DB_PASSWORD` | Database credentials |
| `JWT_SECRET` | Secret key for signing JWTs (min 32 chars) |
| `JWT_EXPIRATION_MS` | Token lifetime in milliseconds (default: 1 hour in prod) |

Run with `--spring.profiles.active=prod` to use production configuration.