<div align="center">

#  BookingForge

**A modern appointment booking REST API built with Spring Boot 4**

[![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![SQL Server](https://img.shields.io/badge/SQL%20Server-2022-CC2927?style=for-the-badge&logo=microsoftsqlserver&logoColor=white)](https://www.microsoft.com/en-us/sql-server)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)

</div>

---

##  Overview

**BookingForge** is a RESTful appointment booking API that allows service providers to define their availability slots and enables clients to book appointments within those time windows. The system includes conflict detection, working hours validation, and JWT-based authentication.

### Key Features

- 🔐 **JWT Authentication** — Secure user registration and login with Bearer token authorization
- 👤 **Provider Management** — Create and manage service providers
- 📅 **Availability Slots** — Providers can define their working hours
- 📌 **Appointment Booking** — Clients can book appointments with automatic conflict detection
- ⏱️ **Overlap Prevention** — Ensures no double-booking through time-slot validation
- 📖 **Swagger / OpenAPI** — Interactive API documentation out of the box
- 🐳 **Docker Ready** — Fully containerized with Docker Compose

---

##  Architecture

```
Controller → Security (JWT Filter) → Service → Repository → Database
```

| Layer | Responsibility |
|-------|---------------|
| **Controller** | REST endpoints, request/response handling |
| **Security** | JWT token generation, validation, and authorization |
| **Service** | Business logic, validation rules |
| **Repository** | Data access via Spring Data JPA |
| **Database** | SQL Server (production) / H2 (tests) |

---

## 🔌 API Endpoints

### Authentication (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/auth/register` | Register a new user |
| `POST` | `/api/v1/auth/login` | Login and receive JWT token |

### Providers (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/providers` | Create a new provider |
| `GET` | `/api/v1/providers` | List all providers |
| `GET` | `/api/v1/providers/{id}` | Get provider by ID |

### Availability (🔒 Requires JWT)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/availabilities` | Add a new availability slot |
| `GET` | `/api/v1/availabilities/provider/{providerId}` | Get provider's availability |

### Appointments (Requires JWT)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/appointments` | Book a new appointment |

>  **Full interactive documentation** available at `/swagger-ui.html` when the application is running.

---

##  Getting Started

### Prerequisites

- **Java 25** or higher
- **Docker & Docker Compose** (for database)
- **Maven 3.9+** (or use the included wrapper)

### 1. Clone the Repository

```bash
git clone https://github.com/meminksr/bookingforge.git
cd bookingforge
```

### 2. Start the Database

```bash
docker compose up -d bookingforge-db
```

### 3. Configure Environment Variables

```bash
export DB_URL="jdbc:sqlserver://localhost:1433;databaseName=master;encrypt=true;trustServerCertificate=true;"
export DB_USERNAME="sa"
export DB_PASSWORD="your_password_here"
```

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

### 5. Run with Docker Compose (Full Stack)

```bash
docker compose up --build
```

---

## 🧪 Testing

The project includes both **unit tests** and **integration tests**:

| Test Type | Count | Description |
|-----------|-------|-------------|
| Unit Tests | 2 | Service layer tests with Mockito |
| Integration Tests | 18 | Full API endpoint tests with H2 in-memory DB |

### Run All Tests

```bash
./mvnw test
```

### Integration Test Strategy

- **`@SpringBootTest`** — Boots the full Spring application context
- **`MockMvc`** — Simulates HTTP requests without starting a server
- **`H2 Database`** — In-memory database for isolated, repeatable tests
- **`@Transactional`** — Automatic rollback after each test
- **Real JWT tokens** — Protected endpoints are tested with actual JWT authentication flow

### Test Coverage

| Controller | Tests | Scenarios Covered |
|------------|-------|-------------------|
| `AuthController` | 4 | Register, login, wrong password, non-existent user |
| `ProviderController` | 4 | Create, list all, get by ID, not found |
| `AvailabilityController` | 5 | Add slot, unauthorized access, invalid time, non-existent provider, list by provider |
| `AppointmentController` | 5 | Book appointment, unauthorized, provider not found, outside hours, overlap detection |

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/meminksr/bookingforge/
│   │   ├── config/             # Security, JWT filter, OpenAPI, data initializer
│   │   ├── controller/         # REST controllers
│   │   ├── domain/             # JPA entities (User, Provider, Appointment, Availability)
│   │   ├── dto/                # Request/Response DTOs
│   │   ├── exception/          # Global exception handler
│   │   ├── repository/         # Spring Data JPA repositories
│   │   ├── security/           # JWT service
│   │   └── service/            # Business logic
│   └── resources/
│       └── application.properties
├── test/
│   ├── java/com/meminksr/bookingforge/
│   │   ├── controller/         # Integration tests
│   │   └── service/            # Unit tests
│   └── resources/
│       └── application-test.properties
```

---

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 25 | Programming language |
| Spring Boot 4.1 | Application framework |
| Spring Security | Authentication & authorization |
| Spring Data JPA | Data access layer |
| JWT (jjwt 0.12.5) | Token-based authentication |
| SQL Server 2022 | Production database |
| H2 | Test database |
| Lombok | Boilerplate reduction |
| SpringDoc OpenAPI | API documentation |
| Docker | Containerization |
| JUnit 5 + MockMvc | Testing framework |

---

##  License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---


