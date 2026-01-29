# Electricity Billing Backend (Microservices)

Spring Boot microservices backend for the Electricity Billing System with **Eureka** (service discovery) and a single **API Gateway** for the frontend.

## Architecture

| Component        | Port | Description                                      |
|----------------|------|--------------------------------------------------|
| **Eureka Server** | 8761 | Service discovery – all services register here   |
| **API Gateway**   | 8080 | Single entry point for frontend (same URL as before) |
| **auth-service**  | 8081 | Login, JWT, admin registration, health          |
| **users-service**| 8082 | Customers, admin CRUD (list/create/delete)      |
| **billing-service** | 8083 | Bills CRUD, view/create/update status            |
| **payments-service** | 8084 | Pay bills                                      |

The frontend continues to use `http://localhost:8080`; the gateway routes to the correct service.

## Prerequisites

- Java 17 or higher  
- Maven 3.6+

## Build

From the `backend` directory:

```bash
mvn clean install
```

## Running (order matters)

Start services in this order so Eureka and the gateway are up before the others:

**1. Eureka Server**
```bash
cd eureka-server && mvn spring-boot:run
```
Dashboard: http://localhost:8761

**2. API Gateway**
```bash
cd api-gateway && mvn spring-boot:run
```
Frontend base URL: http://localhost:8080

**3. Auth Service**
```bash
cd auth-service && mvn spring-boot:run
```

**4. Users Service**
```bash
cd users-service && mvn spring-boot:run
```

**5. Billing Service**
```bash
cd billing-service && mvn spring-boot:run
```

**6. Payments Service**
```bash
cd payments-service && mvn spring-boot:run
```

## Gateway routes (no frontend change)

- `/api/auth/**` → auth-service  
- `/api/admin/register` → auth-service  
- `/api/admin/**` → users-service  
- `/api/customers/**` → users-service  
- `/api/bills/**` → billing-service  
- `/api/payments/**` → payments-service  
- `/api/health` → auth-service  

## Per-service databases

Each service uses its own in-memory H2 database:

- auth-service: `jdbc:h2:mem:authdb` (login table)  
- users-service: `jdbc:h2:mem:usersdb` (customer table)  
- billing-service: `jdbc:h2:mem:billingdb` (bill table)  
- payments-service: `jdbc:h2:mem:paymentsdb` (payment table)  

JWT secret is shared across services via `application.yml` in each service (change in production).

## Legacy monolith

The previous single-module app code remains under `src/` for reference but is not built or run as part of this setup.
