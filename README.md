# BookingForge API 

BookingForge is a high-performance and scalable backend system designed for managing service provider availability and client appointments.

##  Tech Stack

* **Java 26**
* **Spring Boot 4.1.0**
* **Hibernate / JPA**
* **MSSQL**
* **Swagger/OpenAPI**

## Key Features

* **Conflict Management:** Robust backend logic that prevents multiple appointments in the same time slot.
* **Availability Control:** Strict validation logic ensuring appointments are only accepted within predefined business hours.
* **Time Zone Management:** Precise time calculations using ZonedDateTime.
* **Global Exception Handling:** Centralized error management that returns professional JSON responses.

##  API Reference

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| POST | /api/v1/availabilities | Adds a new availability time block. |
| POST | /api/v1/appointments | Creates an appointment with validation. |

##  Getting Started

1. Clone the repository.
2. Configure MSSQL settings in application.properties.
3. Run the application.
4. Access Swagger UI at: http://localhost:8080/swagger-ui/index.html