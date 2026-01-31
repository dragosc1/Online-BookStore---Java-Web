
# Online Bookstore - Java Spring MVP

**Author:** Dragos Ciobanu  
**Coordinator:** Adrian Cernat  
**Date:** December 2025  

This project is a **Minimal Viable Product (MVP)** for an Online Bookstore built with **Java Spring Boot**, using **JWT authentication**, **MySQL** for persistence, and **Swagger/OpenAPI** for API documentation.

---

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Setup & Installation](#setup--installation)
- [Database Configuration](#database-configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Notes](#notes)

---

## Features

- **User Accounts & Roles**  
  - Customers: register, login, manage cart, checkout, leave reviews  
  - Admins: manage books, authors, categories

- **Catalog Browsing & Search**  
  - Search by title, author, category, price, rating  

- **Book Details**  
  - Title, ISBN, authors, price, stock, description  

- **Shopping Cart**  
  - Add, update, remove items  
  - Persistent across sessions  

- **Checkout & Order Management**  
  - Convert cart to order  
  - Payment handling via mock gateway  
  - Inventory adjustments on order placement  

- **Order Tracking**  
  - Status progression: `PLACED → PAID → SHIPPED → DELIVERED`  

- **Reviews & Ratings**  
  - Verified customers can leave reviews  

- **Admin Catalog Management**  
  - CRUD operations for books, authors, categories  

- **Cross-cutting Concerns**  
  - Centralized validation & exception handling (`@ControllerAdvice`)  
  - Transactional checkout operations (`@Transactional`)  
  - Logging via AOP  
  - Swagger API documentation  

---

## Technologies

- Java 17  
- Spring Boot 3  
- Spring Security with JWT  
- Spring Data JPA / Hibernate  
- MySQL 8  
- Maven  
- Swagger/OpenAPI  
- JUnit 5 + Mockito  

---

## Setup & Installation

1. **Clone the repository**

```bash
git clone <your-repo-url>
cd online-bookstore
````

2. **Configure MySQL**

* Create a database:

```sql
CREATE DATABASE online_bookstore;
```

* Update `application.properties` or `application.yml` with your credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/online_bookstore?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

3. **Build the project**

```bash
mvn clean install
```

4. **Run the application**

```bash
mvn spring-boot:run
```

5. **Access Swagger API docs**

```
http://localhost:8080/swagger-ui.html
```

---

## Database Configuration

* **Entities & Relationships:**

| Entity  | Relationship                                |
| ------- | ------------------------------------------- |
| User    | 1 → Many Orders, 1 → 1 Cart                 |
| Order   | 1 → Many OrderItems                         |
| Book    | Many → Many Authors, Many → Many Categories |
| Cart    | 1 → Many CartItems                          |
| Payment | 1 → 1 Order                                 |

---

## API Endpoints Examples

### Authentication

| Method | Endpoint                | Description          |
| ------ | ----------------------- | -------------------- |
| POST   | `/auth/register` | Register a new user  |
| POST   | `/auth/login`    | Login and obtain JWT |

### Books & Catalog

| Method | Endpoint             | Description            |
| ------ | -------------------- | ---------------------- |
| GET    | `/api/v1/books`      | List all books         |
| GET    | `/api/v1/books/{id}` | Get book details by ID |

### Shopping Cart

| Method | Endpoint                                    | Description             |
| ------ | ------------------------------------------- | ----------------------- |
| GET    | `/api/v1/cart`                              | Get current user's cart |
| POST   | `/api/v1/cart/addOrUpdate/item`             | Add or update cart item |
| DELETE | `/api/v1/cart/removeCartItem/item/{bookId}` | Remove item from cart   |

### Orders

| Method | Endpoint                                | Description                                   |
| ------ | --------------------------------------- | --------------------------------------------- |
| POST   | `/api/v1/orders/checkout`               | Checkout cart and create order                |
| GET    | `/api/v1/orders`                        | List orders (current user or admin-specified) |
| GET    | `/api/v1/orders/{orderId}`              | Get order by ID                               |
| PUT    | `/api/v1/orders/updateStatus/{orderId}` | Update order status (admin only)              |

### Reviews

| Method | Endpoint                                                | Description                 |
| ------ | ------------------------------------------------------- | --------------------------- |
| POST   | `/api/v1/reviews/addReview/book/{bookId}`               | Add review to a book        |
| GET    | `/api/v1/reviews/getReviews/book/{bookId}`              | Get all reviews for a book  |
| GET    | `/api/v1/reviews/getReviews/user/{userId}`              | Get all reviews by a user   |
| GET    | `/api/v1/reviews/getReview/book/{bookId}/user/{userId}` | Get review by book & user   |
| PUT    | `/api/v1/reviews/updateReview/{reviewId}`               | Update review (owner only)  |
| DELETE | `/api/v1/reviews/deleteReview/{reviewId}`               | Delete review (owner/admin) |

### Admin Endpoints

| Method | Endpoint                               | Description       |
| ------ | -------------------------------------- | ----------------- |
| POST   | `/api/v1/admin/books/createBook`       | Create a book     |
| PUT    | `/api/v1/admin/books/updateBook/{id}`  | Update a book     |
| DELETE | `/api/v1/admin/books/deleteBook/{id}`  | Delete a book     |
| POST   | `/api/v1/admin/authors/create`         | Create an author  |
| PUT    | `/api/v1/admin/authors/update/{id}`    | Update an author  |
| DELETE | `/api/v1/admin/authors/delete/{id}`    | Delete an author  |
| POST   | `/api/v1/admin/categories/create`      | Create a category |
| PUT    | `/api/v1/admin/categories/update/{id}` | Update a category |
| DELETE | `/api/v1/admin/categories/delete/{id}` | Delete a category |

---

## Testing

* **Unit Tests**: JUnit 5 + Mockito
* **Run tests**:

```bash
mvn test
```

* Tests cover controllers, services, and edge cases (NPEs, authorization, CRUD operations).

---

## Notes

* JWT token must be included in the `Authorization` header for protected endpoints:

```
Authorization: Bearer <token>
```

* Admin endpoints require users with role `ADMIN`.
* Cart and order operations are **transactional** to ensure data consistency.
* Mock payment gateway is implemented for demonstration purposes only.

---

## License

MIT License

---
