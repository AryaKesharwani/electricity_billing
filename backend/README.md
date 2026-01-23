# Electricity Billing Backend

Spring Boot backend application for the Electricity Billing System.

## Prerequisites

- Java 17 or higher
- Maven 3.6+

## Running the Application

1. Build the project:
```bash
mvn clean install
```

2. Run the application:
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## API Endpoints

- `GET /api/health` - Health check endpoint

## Swagger/OpenAPI Documentation

The API documentation is available via Swagger UI:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

You can explore and test all available endpoints through the Swagger UI interface.

## H2 Database Console

For development, H2 console is available at `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

