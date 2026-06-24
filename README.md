# BookingForge API

![Java](https://img.shields.io/badge/Java-17-orange.svg) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-brightgreen.svg) ![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg) ![MSSQL](https://img.shields.io/badge/Database-MSSQL-red.svg)

**BookingForge** is an enterprise-grade, containerized REST API architected for scalable appointment scheduling and
provider availability management. Built with **Spring Boot** and secured by **JWT**, it ensures data integrity through
sophisticated relational database mapping and optimistic locking mechanisms.

---

## System Architecture & Features

The API is designed to handle complex business logic for service-based platforms, ensuring secure and conflict-free
bookings.

* **🔒 Stateless Security (JWT):** Robust authentication and endpoint authorization using Spring Security. Role-based
  access control restricts sensitive operations.
* **🛡️ Concurrency Control:** Implemented `@Version` (Optimistic Locking) via Hibernate to eliminate race conditions and
  prevent double-booking of availability slots.
* **🗄️ Advanced Data Modeling:** Highly normalized **Microsoft SQL Server** schema with perfectly mapped one-to-many and
  many-to-one JPA relationships between Providers, Availabilities, and Appointments.
* **🐳 Fully Containerized:** Infrastructure as Code (IaC) approach. The entire ecosystem (Application + MSSQL Database)
  is orchestrated via Docker Compose for zero-configuration deployments.
* **✅ Input Validation & Exception Handling:** Comprehensive Global Exception Handler to return clean, standardized HTTP
  responses (RFC 7807) and prevent database null-constraint violations.

---

## Technology Stack

| Category              | Technology                                               |
|:----------------------|:---------------------------------------------------------|
| **Core Framework**    | Java 17, Spring Boot 3, Spring Web                       |
| **Security**          | Spring Security, JSON Web Tokens (JWT)                   |
| **Database & ORM**    | Microsoft SQL Server (MSSQL), Spring Data JPA, Hibernate |
| **DevOps & Tools**    | Docker, Docker Compose, Maven, Lombok                    |
| **API Documentation** | OpenAPI 3.0 (Swagger UI)                                 |

---

## Local Environment Setup

BookingForge is built to be run instantly on any machine with Docker installed, eliminating the "it works on my machine"
problem.

### Prerequisites

* [Docker & Docker Compose](https://www.docker.com/)

### Installation Steps

1. **Clone the repository**

```bash
   git clone [https://github.com/meminksr/bookingforge.git](https://github.com/meminksr/bookingforge.git)
   cd bookingforge
   
2. **Initialize the Dockerized Ecosystem**
    This command pulls the MSSQL image, provisions the database, builds the Spring Boot application, and links them within an isolated network.
```bash
    docker-compose up --build
    ```

3. **Explore the Interactive API Docs**
    Once the console outputs `Started BookingforgeApplication`, the Swagger UI will be available at:
```text
    http://localhost:8080/swagger-ui.html
    ```


##  API Reference

Here are the core endpoints provided by the BookingForge API. Authentication is required for protected routes via the
`Authorization: Bearer <token>` header.

| Method   | Endpoint                   | Description                   |  Auth  |
|:---------|:---------------------------|:------------------------------|:------:|
| **POST** | **/api/v1/auth/register**  | Register a new user           |   ❌    |
| **POST** | **/api/v1/auth/login**     | Authenticate and obtain JWT   |   ❌    |
| **POST** | **/api/v1/providers**      | Create a new service provider | 🔒 Yes |
| **GET**  | **/api/v1/providers**      | Retrieve a list of providers  | 🔒 Yes |
| **POST** | **/api/v1/availabilities** | Allocate working hours        | 🔒 Yes |
| **POST** | **/api/v1/appointments**   | Secure a booking              | 🔒 Yes |



