## Online Bookstore Spring Boot

A RESTful online bookstore API built with Spring Boot, Spring Security (Basic Auth), JPA, and H2 database.

---

##  Project Structure

```
src/main/java/onlinebookstore/application/
├── Application.java
├── basic/
│   ├── SecurityConfig.java       # Basic Auth + endpoint rules
│   └── DataSeeder.java 
│   └── CustomUserDetailsService.java          # Sample data on startup
├── controller/
│   ├── AuthController.java
│   ├── BookController.java
│   └── OrderController.java
├── dto/
│   └── BookstoreDTO.java         # All request/response DTOs
├── entity/
│   ├── User.java
│   ├── Book.java
│   ├── Order.java
│   └── OrderItem.java
├── exception/
│   └── BookstoreExceptions.java  # Custom exceptions + global handler
├── repositories/
│   ├── UserRepository.java
│   ├── BookRepository.java
│   └── CartRepository.java 
└── service/
    ├── AuthService.java
    ├── BookService.java
    └── CartService.java
```

---

## 🚀 Running the Application

**Prerequisites:** Java 23+, Maven 3.4+

```bash
cd bookstore
mvn spring-boot:run
```

Server starts at: `http://localhost:8080`
H2 Console:       `http://localhost:8080/h2-console`

---

## 🔑 Default Credentials (seeded on startup)

| Role  | Username | Password   |
|-------|----------|------------|
| ADMIN | admin    | admin123   |
| USER  | ram      | user123    |

---

## 🔐 Authentication

This API uses **HTTP Basic Authentication**.

Include the `Authorization` header in every protected request:

```
Authorization: Basic <base64(username:password)>
```

Example with curl:
```bash
curl -u john:user123 http://localhost:8080/api/auth/me
```

---

## 📡 API Endpoints

### Auth

| Method | Endpoint         | Auth     | Description          |
|--------|------------------|----------|----------------------|
| POST   |/api/auth/register| Public   | Register new user    |
| GET    | /api/auth/me     | User     | Get current user     |

**Register:**
```json
POST /api/auth/register
{
  "username": "alice",
  "email": "alice@example.com",
  "password": "secret123"
}
```

---

### Books

| Method | Endpoint                      | Auth   | Description           |
|--------|-------------------------------|--------|-----------------------|
| GET    | /api/books                    | Public | Get all books         |
| GET    | /api/books/{id}               | Public | Get book by ID        |
| GET    | /api/books/search?keyword=X   | Public | Search books          |
| GET    | /api/books/category/{cat}     | Public | Books by category     |
| POST   | /api/books                    | ADMIN  | Add a book            |
| PUT    | /api/books/{id}               | ADMIN  | Update a book         |
| DELETE | /api/books/{id}               | ADMIN  | Delete a book         |

**Add Book (Admin):**
```json
POST /api/books
Authorization: Basic YWRtaW46YWRtaW4xMjM=
{
  "title": "Spring Boot in Action",
  "author": "Craig Walls",
  "isbn": "978-1617292545",
  "description": "Covers Spring Boot essentials.",
  "price": 49.99,
  "stockQuantity": 25,
  "category": "Programming"
}
```

---

### Orders

| Method | Endpoint                        | Auth  | Description             |
|--------|---------------------------------|-------|-------------------------|
| POST   | /api/orders                     | User  | Place an order          |
| GET    | /api/orders                     | User  | Get my orders           |
| GET    | /api/orders/{id}                | User  | Get order by ID         |
| GET    | /api/orders/admin/all           | ADMIN | Get all orders          |
| PATCH  | /api/orders/admin/{id}/status?status=X | ADMIN | Update order status |

**Place Order:**
```json
POST /api/orders
Authorization: Basic am9objp1c2VyMTIz
{
  "items": [
    { "bookId": 1, "quantity": 2 },
    { "bookId": 3, "quantity": 1 }
  ]
}
```

**Order Statuses:** `PENDING` → `CONFIRMED` → `SHIPPED` → `DELIVERED` / `CANCELLED`

---

## 🛡️ Role Permissions

| Endpoint              | USER | ADMIN |
|-----------------------|------|-------|
| View books            | ✅   | ✅    |
| Place orders          | ✅   | ✅    |
| View own orders       | ✅   | ✅    |
| Add/Edit/Delete books | ❌   | ✅    |
| View all orders       | ❌   | ✅    |
| Update order status   | ❌   | ✅    |

---

## 🔄 Sample curl Commands

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","email":"alice@test.com","password":"pass123"}'

# Get all books (public)
curl http://localhost:8080/api/books

# Search books
curl "http://localhost:8080/api/books/search?keyword=clean"

# Place an order (as john)
curl -u john:user123 -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"items":[{"bookId":1,"quantity":1}]}'

# View my orders
curl -u john:user123 http://localhost:8080/api/orders

# Admin: update order status
curl -u admin:admin123 -X PATCH \
  "http://localhost:8080/api/orders/admin/1/status?status=CONFIRMED"
```

---

## 🗄️ H2 Database Console

Access at `http://localhost:8080/h2-console`

| Field    | Value              |
|----------|--------------------|
| JDBC URL | jdbc:h2:mem:bookstoredb |
| Username | sa                 |
| Password | password           |

---

## 🧰 Tech Stack

- **Spring Boot 3.2**
- **Spring Security** (HTTP Basic Auth)
- **Spring Data JPA** + Hibernate
- **H2** (in-memory database)
- **Lombok**
- **Bean Validation**

