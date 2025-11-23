# Highload E-commerce Backend Project Documentation

## 1. Project Overview

**Project Name:** E-commerce Highload Backend
**Development Tools / Frameworks:**

* Java 17
* Spring Boot 3
* Spring Data JPA
* Spring Security + JWT
* Hibernate
* PostgreSQL / H2 / MySQL
* Swagger UI
* Lombok
* Spring Cache (Caffeine)

**Project Objective:**
Implement a backend system for an e-commerce platform, supporting user management, product and category management, order processing, basic role-based access control, and caching. The system is designed to be performant under high load scenarios.

## 2. System Architecture

```
Client (Postman / Frontend)
        |
   HTTP REST API
        |
----------------------------
|      Controller Layer     |
----------------------------
        |
----------------------------
|        Service Layer      |
----------------------------
        |
----------------------------
|        Repository Layer   |
----------------------------
        |
     Database (PostgreSQL)
```

* **Controller Layer:** Handles HTTP requests and routing.
* **Service Layer:** Contains business logic, caching, inventory management, and pagination/filtering.
* **Repository Layer:** Interacts with the database via JPA/Hibernate.
* **Security:** JWT authentication and role-based access control.
* **Caching:** Improves response times for frequently requested data.

## 3. Functional Modules

### 3.1 User Authentication & Authorization

* Endpoint: `/api/auth/login` returns JWT token.
* Roles:

  * `ADMIN`: Access to `/api/admin/**`
  * `USER`: Access to `/api/user/**`
  * `PUBLIC`: Access to `/api/public/**` without authentication.

### 3.2 Product Management

* `GET /api/products` — List all products (cached)
* `GET /api/products/{id}` — Get a single product
* `POST /api/products` — Create a product
* `PUT /api/products/{id}` — Update a product
* `DELETE /api/products/{id}` — Delete a product (soft delete possible)

### 3.3 Category Management

* `GET /api/categories` — List all categories (cached)
* `GET /api/categories/{id}` — Get category by ID
* `POST /api/categories` — Create category
* `PUT /api/categories/{id}` — Update category
* `DELETE /api/categories/{id}` — Delete category

### 3.4 Appliance Management (Example with pagination and filtering)

* `GET /api/appliances` — List appliances with pagination and search by name
* `GET /api/appliances/{id}` — Get appliance details
* `POST /api/appliances` — Create appliance
* `PUT /api/appliances/{id}` — Update appliance
* `DELETE /api/appliances/{id}` — Soft delete

### 3.5 Order Management

* Create orders with stock deduction and total price calculation
* `POST /api/orders` — Place order
* Extendable: Query order list, filter by user or status

## 4. Database Design

### 4.1 Products Table `products`

| Column      | Type    | Description             |
| ----------- | ------- | ----------------------- |
| id          | Long    | Primary Key             |
| name        | String  | Product name            |
| description | String  | Product description     |
| price       | Decimal | Product price           |
| quantity    | Integer | Inventory count         |
| category_id | Long    | Foreign key to category |

### 4.2 Categories Table `categories`

| Column | Type   | Description   |
| ------ | ------ | ------------- |
| id     | Long   | Primary Key   |
| name   | String | Category name |

### 4.3 Users Table `users`

| Column   | Type   | Description                       |
| -------- | ------ | --------------------------------- |
| id       | Long   | Primary Key                       |
| email    | String | Email address                     |
| password | String | Password (plain text / encrypted) |
| role     | String | User role (ADMIN/USER)            |

### 4.4 Orders Table `orders`

| Column       | Type    | Description                        |
| ------------ | ------- | ---------------------------------- |
| id           | Long    | Primary Key                        |
| user_id      | Long    | Foreign key to users               |
| appliance_id | Long    | Foreign key to products/appliances |
| quantity     | Integer | Quantity ordered                   |
| total_price  | Decimal | Total price of order               |

## 5. Security Design

* **JWT Authentication:** Token contains subject (username) and role.
* **Access Control:**

  * `/api/admin/**` — ADMIN only
  * `/api/user/**` — USER or ADMIN
  * `/api/public/**` — No authentication required
* **JwtAuthenticationFilter** parses JWT and sets Spring Security context.

## 6. Caching Strategy

* Cached endpoints: `/api/products` and `/api/categories`
* Cache implementation: Spring Cache (Caffeine)
* Cache refresh:

  * On create, update, delete operations
  * Automatic expiration after 1 hour

## 7. Sample Data

### Products

```
1 | Laptop | RTX4060, Intel i7, 32GB | 500000 | 123 | 1
2 | Intel | RTX4060, Intel i5, 16GB | 450000 | 100 | 1
3 | Huawei | Newest version of Huawei product | 550000 | 150 | 2
```

### Categories

```
1 | Electronics
2 | Mobile
```

### Users

```
admin@example.com | 123456 | ADMIN
user@example.com | 123456 | USER
```

## 8. API Examples

### Login

```
POST /api/auth/login
Body: { "email": "admin@example.com", "password": "123456" }
Response: { "token": "...", "email": "...", "role": "ADMIN" }
```

### Get All Products

```
GET /api/products
Header: Authorization: Bearer <token>
```

### Create Order

```
POST /api/orders
Header: Authorization: Bearer <token>
Body: { "userId": 2, "applianceId": 1, "quantity": 1 }
```

## 9. Project Setup

1. Clone the project repository
2. Configure database (H2 or PostgreSQL)
3. Run `Application.java`
4. Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## 10. Logging and Exception Handling

* Global exception handler returns JSON error responses
* Key business operations are logged (product fetch, order creation, login attempts)

## 11. Summary

The project implements a highload-ready backend for an e-commerce system with role-based access, caching, and basic CRUD operations. It can serve as a homework submission for the backend course, demonstrating Spring Boot, JWT, caching, and database integration.
