# ⚡ Electricity Billing System - Full-Stack Microservices Project

> A production-ready, enterprise-grade electricity billing management system built with **Spring Boot Microservices** and **Angular**

---

## 📋 Project Overview

This is a comprehensive **full-stack microservices application** for managing electricity billing operations, including customer management, bill generation, and payment processing. The system demonstrates modern software architecture patterns, cloud-native design, and industry best practices.

**Key Highlights:**
- 🏗️ **Microservices Architecture** with 6 independent services
- 🔐 **JWT-based Authentication & Authorization**
- 🌐 **API Gateway Pattern** with centralized routing
- 🔍 **Service Discovery** using Netflix Eureka
- 💾 **Database per Service** pattern
- 🎨 **Modern Angular SPA** with reactive programming
- 🔄 **RESTful API** design
- 📦 **Containerization-ready** architecture

---

## 🛠️ Technology Stack

### Backend
| Technology | Purpose | Version |
|------------|---------|---------|
| **Java** | Core Language | 17 |
| **Spring Boot** | Framework | 3.2.0 |
| **Spring Cloud Gateway** | API Gateway | 2023.0.0 |
| **Spring Cloud Netflix Eureka** | Service Discovery | 2023.0.0 |
| **Spring Data JPA** | ORM | 3.2.0 |
| **H2 Database** | Persistent Storage | Runtime |
| **JWT (JJWT)** | Authentication | 0.11.5 |
| **Lombok** | Boilerplate Reduction | 1.18.30 |
| **Maven** | Build Tool | 3.9+ |
| **Hibernate** | JPA Implementation | 6.x |

### Frontend
| Technology | Purpose | Version |
|------------|---------|---------|
| **Angular** | SPA Framework | 18.0 |
| **TypeScript** | Language | 5.4 |
| **RxJS** | Reactive Programming | 7.8 |
| **Angular Router** | Navigation | 18.0 |
| **HttpClient** | API Communication | 18.0 |

### DevOps & Tools
- **Git** - Version Control
- **VS Code** - IDE with custom tasks
- **Maven** - Dependency Management
- **npm** - Frontend Package Manager

---

## 🏗️ System Architecture

### Microservices Architecture Diagram

```
                                    ┌─────────────────┐
                                    │   Angular SPA   │
                                    │  (Port: 4200)   │
                                    └────────┬────────┘
                                             │
                                    HTTP/REST Calls
                                             │
                    ┌────────────────────────▼────────────────────────┐
                    │         API Gateway (Port: 8080)                │
                    │  - CORS Configuration                           │
                    │  - Request Routing                              │
                    │  - Single Entry Point                           │
                    └────────────┬──────────┬──────────┬──────────────┘
                                 │          │          │
                    ┌────────────▼──┐    ┌──▼─────┐  ┌▼────────────┐
                    │  Auth Service │    │ Users  │  │  Billing    │
                    │  (Port: 8081) │    │ (8082) │  │   (8083)    │
                    └───────────────┘    └────────┘  └─────────────┘
                                              │
                                         ┌────▼────────┐
                                         │  Payments   │
                                         │  (Port:8084)│
                                         └─────────────┘
                                              │
                    ┌─────────────────────────▼─────────────────────┐
                    │        Eureka Server (Port: 8761)             │
                    │        Service Discovery & Registration       │
                    └────────────────────────────────────────────────┘
```

### Service Breakdown

| Service | Port | Responsibility | Database |
|---------|------|---------------|----------|
| **Eureka Server** | 8761 | Service registry and discovery | - |
| **API Gateway** | 8080 | Single entry point, routing, CORS | - |
| **Auth Service** | 8081 | Authentication, JWT, Admin registration | authdb |
| **Users Service** | 8082 | Customer CRUD, Admin operations | usersdb |
| **Billing Service** | 8083 | Bill management, status updates | billingdb |
| **Payments Service** | 8084 | Payment processing | paymentsdb |

---

## ✨ Key Features Implemented

### 1. Authentication & Authorization
- **JWT-based** stateless authentication
- **Role-based access control** (Admin vs Customer)
- Secure password handling
- Token expiration management
- HTTP Interceptor for automatic token injection

### 2. Microservices Communication
- **Service Discovery** using Eureka
- **Load-balanced** inter-service communication
- **RestTemplate** with `@LoadBalanced` annotation
- Internal APIs for service-to-service calls
- Compensating transactions for data consistency

### 3. Database Architecture
- **Database per Service** pattern
- Persistent H2 file-based storage
- Separate schemas for each microservice
- H2 Console for debugging
- JPA/Hibernate for ORM

### 4. API Design
- **RESTful** endpoints
- Proper HTTP methods (GET, POST, PATCH, DELETE)
- JSON request/response bodies
- Consistent error handling
- HTTP status codes (200, 201, 409, 404, 401, etc.)

### 5. Frontend Architecture
- **Component-based** Angular application
- **Reactive programming** with RxJS Observables
- **Route guards** for authentication
- **Standalone components** (Angular 18+)
- Session storage for state management
- Form validation

---

## 🔑 Technical Implementation Highlights

### 1. API Gateway Pattern
```java
// Centralized routing configuration
spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: lb://auth-service  # Load-balanced via Eureka
          predicates:
            - Path=/api/auth/**
```

### 2. Service Discovery
```java
@EnableEurekaServer  // Eureka Server
@EnableEurekaClient  // Microservices
```

### 3. JWT Authentication
```java
// Token generation and validation
String token = Jwts.builder()
    .setSubject(email)
    .claim("userType", userType)
    .setIssuedAt(new Date())
    .setExpiration(new Date(System.currentTimeMillis() + expiration))
    .signWith(key, SignatureAlgorithm.HS256)
    .compact();
```

### 4. Inter-Service Communication
```java
@LoadBalanced
@Bean
public RestTemplate restTemplate() {
    return new RestTemplate();
}

// Service-to-service call
String url = "http://users-service/internal/customers/customer-name";
CustomerNameResponse response = restTemplate.getForObject(url, CustomerNameResponse.class);
```

### 5. Database Persistence
```yaml
spring:
  datasource:
    url: jdbc:h2:file:./data/authdb;AUTO_SERVER=TRUE
  jpa:
    hibernate:
      ddl-auto: update  # Auto schema management
```

### 6. CORS Configuration
```java
// Reactive CORS filter for API Gateway
@Bean
public CorsWebFilter corsWebFilter() {
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowedOrigins(List.of("http://localhost:4200"));
    // ... configuration
}
```

---

## 📊 Data Models

### Core Entities

**Login** (Auth Service)
```java
- userId: Long (PK)
- email: String (Unique)
- password: String (Encrypted)
- userType: Enum (ADMIN, CUSTOMER)
- consumerId: String (for customers)
- status: Enum (ACTIVE, DEACTIVATED)
```

**Customer** (Users Service)
```java
- userId: Long (PK)
- consumerId: String (Unique)
- name: String
- email: String
- phoneNumber: String
- address: String
```

**Bill** (Billing Service)
```java
- billId: Long (PK)
- consumerId: String
- billDate: LocalDate
- dueDate: LocalDate
- unitsConsumed: Integer
- amount: BigDecimal
- status: Enum (PAID, UNPAID, OVERDUE)
- description: String
```

**Payment** (Payments Service)
```java
- paymentId: Long (PK)
- billId: Long
- consumerId: String
- amount: BigDecimal
- paymentDate: LocalDate
- paymentMethod: String
```

---

## 🔄 User Workflows

### Admin Workflow
1. **Register** → Create admin account
2. **Login** → Get JWT token
3. **View Customers** → List all customers
4. **Create Customer** → Add new customer
5. **Create Bill** → Generate bill for customer
6. **View All Bills** → Monitor all bills
7. **Update Bill Status** → Mark as PAID/UNPAID/OVERDUE
8. **Delete Customer** → Remove customer and related data

### Customer Workflow
1. **Register** → Create account (or admin creates)
2. **Login** → Get JWT token
3. **View Bills** → See personal bills
4. **Pay Bill** → Process payment
5. **View Payment History** → Track payments

---

## 🎯 Microservices Design Patterns Implemented

### 1. **API Gateway Pattern**
- Single entry point for all client requests
- Centralized routing logic
- Cross-cutting concerns (CORS, security)

### 2. **Service Registry Pattern**
- Dynamic service discovery
- Health checking
- Load balancing

### 3. **Database per Service**
- Each service owns its data
- Loose coupling
- Independent scaling

### 4. **Saga Pattern (Choreography)**
- Distributed transactions across services
- Compensating actions (delete customer → cascade deletes)

### 5. **Circuit Breaker (Implicit)**
- Retry logic via RestTemplate
- Graceful degradation

---

## 🚀 Running the Application

### Prerequisites
- Java 17+
- Maven 3.9+
- Node.js 18+
- npm 10+

### Quick Start (3 commands)

**1. Build Backend**
```bash
cd backend
mvn clean install
```

**2. Start Services (In Order)**
```bash
# Use VS Code Tasks or manually:
cd eureka-server && mvn spring-boot:run  # Wait 15 seconds
cd api-gateway && mvn spring-boot:run    # Wait 15 seconds
cd auth-service && mvn spring-boot:run
cd users-service && mvn spring-boot:run
cd billing-service && mvn spring-boot:run
cd payments-service && mvn spring-boot:run
```

**3. Start Frontend**
```bash
cd frontend
npm install  # First time only
npm start
```

**Access:**
- Frontend: http://localhost:4200
- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:8080
- H2 Consoles: http://localhost:808X/h2-console

---

## 📈 Scalability & Performance Considerations

### Current Implementation
✅ Stateless services (JWT)  
✅ Independent deployments  
✅ Database per service  
✅ Load-balanced inter-service calls  
✅ Persistent data storage  

### Production Enhancements (Future)
🔲 Replace H2 with PostgreSQL/MySQL  
🔲 Add Redis for caching  
🔲 Implement Circuit Breaker (Resilience4j)  
🔲 Add distributed tracing (Zipkin/Jaeger)  
🔲 Containerize with Docker  
🔲 Orchestrate with Kubernetes  
🔲 Add API rate limiting  
🔲 Implement event-driven architecture (Kafka/RabbitMQ)  
🔲 Add monitoring (Prometheus + Grafana)  
🔲 CI/CD pipeline (Jenkins/GitHub Actions)  

---

## 🧪 Testing Strategy

### Current Coverage
- Manual testing via UI
- API testing via Postman
- H2 Console for data verification
- Eureka Dashboard for service health

### Planned Testing
- **Unit Tests**: JUnit + Mockito
- **Integration Tests**: Spring Boot Test
- **E2E Tests**: Protractor/Cypress
- **Load Tests**: JMeter/Gatling
- **Contract Tests**: Pact

---

## 🛡️ Security Features

1. **Authentication**: JWT tokens
2. **Authorization**: Role-based (ADMIN/CUSTOMER)
3. **Password Security**: BCrypt hashing
4. **CORS**: Configured at API Gateway
5. **Input Validation**: Backend validation
6. **SQL Injection Prevention**: JPA/Hibernate parameterized queries
7. **Session Management**: Stateless JWT

### Production Security Checklist
- [ ] HTTPS/TLS everywhere
- [ ] API rate limiting
- [ ] Input sanitization
- [ ] SQL injection prevention (already using JPA)
- [ ] XSS prevention
- [ ] CSRF protection
- [ ] Secret management (Vault)
- [ ] Audit logging

---

## 📚 Project Structure

```
electricity_billing/
├── backend/
│   ├── eureka-server/          # Service discovery
│   ├── api-gateway/            # Entry point
│   ├── auth-service/           # Authentication
│   ├── users-service/          # Customer management
│   ├── billing-service/        # Bill operations
│   ├── payments-service/       # Payment processing
│   ├── pom.xml                 # Parent POM
│   └── DATABASE.md             # DB documentation
├── frontend/
│   ├── src/app/
│   │   ├── services/           # API services
│   │   ├── models/             # TypeScript interfaces
│   │   ├── guards/             # Route guards
│   │   ├── interceptors/       # HTTP interceptors
│   │   └── [components]/       # UI components
│   └── package.json
├── .vscode/
│   ├── tasks.json              # VS Code tasks
│   ├── QUICKSTART.md           # Quick start guide
│   ├── PLATFORM-SETUP.md       # Cross-platform setup
│   └── H2-CONSOLE-ACCESS.md    # Database access
└── README.md
```

---

## 🎓 Learning Outcomes / Skills Demonstrated

### Backend Development
✅ **Spring Boot** ecosystem mastery  
✅ **Microservices** architecture design  
✅ **RESTful API** development  
✅ **Service discovery** with Eureka  
✅ **API Gateway** implementation  
✅ **JWT authentication** from scratch  
✅ **JPA/Hibernate** ORM  
✅ **Exception handling** strategies  
✅ **Inter-service communication**  
✅ **Database design** and normalization  

### Frontend Development
✅ **Angular 18** with standalone components  
✅ **TypeScript** programming  
✅ **RxJS** reactive programming  
✅ **HTTP interceptors** for auth  
✅ **Route guards** for security  
✅ **Component architecture**  
✅ **State management** with session storage  
✅ **Responsive design**  

### Software Engineering
✅ **Design patterns** (Gateway, Registry, Database per Service)  
✅ **Clean code** principles  
✅ **Git** version control  
✅ **Documentation** (technical + user guides)  
✅ **Cross-platform** development  
✅ **Problem-solving** (CORS, JWT, service communication)  
✅ **Debugging** distributed systems  

---

## 💡 Architectural Decisions & Trade-offs

### 1. Why Microservices over Monolith?
**✅ Chosen:** Microservices  
**Reason:** Demonstrates modern architecture, allows independent scaling, easier to maintain  
**Trade-off:** Increased complexity, but more impressive for interviews

### 2. Why H2 over PostgreSQL?
**✅ Chosen:** H2 (persistent file-based)  
**Reason:** Easy setup, no external dependencies, portable  
**Trade-off:** Not production-ready, but easily replaceable

### 3. Why Eureka over Consul/Kubernetes?
**✅ Chosen:** Netflix Eureka  
**Reason:** Spring Cloud native, widely used in enterprise, easier to demonstrate  
**Trade-off:** Less modern than Kubernetes, but more focused on Spring ecosystem

### 4. Why JWT over OAuth2?
**✅ Chosen:** JWT  
**Reason:** Simpler to implement, stateless, demonstrates token-based auth  
**Trade-off:** Manual implementation, but shows deeper understanding

### 5. Why REST over GraphQL/gRPC?
**✅ Chosen:** REST  
**Reason:** Industry standard, HTTP-based, easier to test and debug  
**Trade-off:** More verbose, but more universally understood

---

## 🎤 Interview Talking Points

### Technical Depth
1. **"How did you handle authentication across microservices?"**
   - JWT tokens validated by each service
   - Shared secret key across services
   - HTTP interceptor automatically adds token

2. **"How do services communicate?"**
   - RestTemplate with `@LoadBalanced`
   - Eureka for service discovery
   - Internal APIs for service-to-service
   - Cascading deletes via REST calls

3. **"What happens if a service goes down?"**
   - Eureka shows service status
   - RestTemplate retries
   - API Gateway routes to healthy instances

4. **"How did you ensure data consistency?"**
   - Database per service
   - Compensating transactions
   - Example: Delete customer → calls auth, billing, payments

5. **"Why this tech stack?"**
   - Spring Boot: Industry standard, rich ecosystem
   - Angular: Modern SPA framework
   - H2: Development ease, production upgradable

### Problem-Solving
- **CORS Issues**: Solved by centralizing CORS at Gateway
- **Duplicate Headers**: Removed CORS from services
- **Service Startup Order**: Eureka must start first
- **JWT Validation**: Shared secret across services
- **Platform Independence**: Made tasks.json cross-platform

### Scalability
- Current: Single-instance per service
- Future: Kubernetes horizontal scaling, Redis cache, Message queues

---

## 📞 Contact & Demo

**Project Repository:** [GitHub Link]  
**Live Demo:** [If deployed]  
**Email:** [Your Email]  
**LinkedIn:** [Your LinkedIn]  

---

## 📄 License

This project is for educational and portfolio purposes.

---

## 🙏 Acknowledgments

Built with:
- Spring Framework Team
- Angular Team
- Netflix OSS (Eureka)
- Open Source Community

---

**Last Updated:** January 2026  
**Version:** 2.0 (Microservices Architecture)
