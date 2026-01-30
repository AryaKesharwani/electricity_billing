# ⚡ Interview Cheat Sheet - Electricity Billing System

> Quick reference for interview questions

---

## 🎯 Project Elevator Pitch (30 seconds)

*"I built a full-stack electricity billing system using Spring Boot microservices and Angular. The backend consists of 6 services - Eureka for service discovery, an API Gateway for routing, and 4 business services for authentication, user management, billing, and payments. Each service has its own database following the database-per-service pattern. The frontend is a modern Angular SPA with JWT authentication, role-based access control, and a responsive UI. The system demonstrates enterprise-grade architecture patterns like service discovery, API gateway, and inter-service communication."*

---

## 🏗️ Architecture Quick Facts

| Component | Technology | Port |
|-----------|-----------|------|
| Frontend | Angular 18 | 4200 |
| API Gateway | Spring Cloud Gateway | 8080 |
| Service Discovery | Netflix Eureka | 8761 |
| Auth Service | Spring Boot | 8081 |
| Users Service | Spring Boot | 8082 |
| Billing Service | Spring Boot | 8083 |
| Payments Service | Spring Boot | 8084 |

**Request Flow:** Browser → API Gateway → Microservice → Database

---

## 💻 Tech Stack Summary

### Backend
- **Java 17** + **Spring Boot 3.2**
- **Spring Cloud** (Gateway, Eureka)
- **Spring Data JPA** + **Hibernate**
- **JWT** for authentication
- **H2 Database** (persistent)
- **Maven** for build

### Frontend
- **Angular 18** + **TypeScript 5.4**
- **RxJS** for reactive programming
- **HTTP Interceptors** for auth
- **Route Guards** for security

---

## 🔑 Key Features Implemented

1. ✅ **Microservices Architecture** (6 services)
2. ✅ **Service Discovery** (Eureka)
3. ✅ **API Gateway Pattern** (Single entry point)
4. ✅ **JWT Authentication** (Stateless)
5. ✅ **Role-Based Access** (Admin/Customer)
6. ✅ **Database per Service** (4 databases)
7. ✅ **Inter-Service Communication** (REST)
8. ✅ **CORS Configuration** (Centralized)
9. ✅ **Persistent Storage** (File-based H2)
10. ✅ **Reactive Frontend** (RxJS Observables)

---

## 🎤 Common Interview Questions & Answers

### Q1: "Why did you choose microservices over a monolith?"

**Answer:** 
- **Scalability**: Services can scale independently
- **Maintainability**: Smaller, focused codebases
- **Technology flexibility**: Can use different tech per service
- **Fault isolation**: One service failure doesn't crash everything
- **Modern architecture**: Industry standard, good for portfolio

---

### Q2: "How do your microservices communicate?"

**Answer:**
- **Synchronous**: RestTemplate with `@LoadBalanced` annotation
- **Service Discovery**: Eureka resolves service locations
- **Load Balancing**: Built-in client-side load balancing
- **Internal APIs**: Special endpoints for service-to-service calls
- **Example**: Users service calls Auth service to create login when registering customer

```java
@LoadBalanced
@Bean
public RestTemplate restTemplate() { return new RestTemplate(); }

// Call by service name, not IP
String url = "http://auth-service/internal/logins";
```

---

### Q3: "How does authentication work across services?"

**Answer:**
1. User logs in → Auth service validates → Returns JWT token
2. Frontend stores token in session storage
3. HTTP interceptor adds token to all requests
4. API Gateway forwards requests with token
5. Each service validates JWT using shared secret
6. Token contains: email, userType, consumerId

```java
// JWT validation in each service
String token = authHeader.substring(7);
Claims claims = Jwts.parserBuilder()
    .setSigningKey(key)
    .build()
    .parseClaimsJws(token)
    .getBody();
```

---

### Q4: "What happens if a service goes down?"

**Answer:**
- **Eureka Dashboard**: Shows service health (UP/DOWN)
- **Retry Logic**: RestTemplate attempts retries
- **Load Balancing**: Routes to healthy instances
- **Graceful Degradation**: Services can handle partial failures
- **Example**: If Users service is down, Auth can still validate logins

---

### Q5: "How do you ensure data consistency across services?"

**Answer:**
- **Database per Service**: Each service owns its data
- **No Shared Database**: Prevents tight coupling
- **Compensating Transactions**: Saga pattern (choreography)
- **Example**: Delete customer
  1. Delete from Users DB
  2. Call Auth service → delete login
  3. Call Billing service → delete bills
  4. Call Payments service → delete payments
  5. If any fails → rollback (delete customer record)

---

### Q6: "Explain your API Gateway implementation"

**Answer:**
- **Technology**: Spring Cloud Gateway (reactive)
- **Purpose**: 
  - Single entry point for frontend
  - Centralized routing
  - CORS configuration
  - Cross-cutting concerns
- **Routes**: Path-based routing to services
  - `/api/auth/**` → auth-service
  - `/api/customers/**` → users-service
  - `/api/bills/**` → billing-service
  - `/api/payments/**` → payments-service

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: lb://auth-service  # Load balanced
          predicates:
            - Path=/api/auth/**
```

---

### Q7: "What design patterns did you use?"

**Answer:**
1. **API Gateway Pattern**: Single entry point
2. **Service Registry Pattern**: Eureka for discovery
3. **Database per Service**: Loose coupling
4. **Saga Pattern**: Distributed transactions
5. **Circuit Breaker**: Implicit via RestTemplate retries
6. **Repository Pattern**: Spring Data JPA
7. **Singleton**: Spring Beans
8. **Dependency Injection**: Spring IoC
9. **Observer Pattern**: RxJS in frontend
10. **Interceptor Pattern**: HTTP Interceptor for JWT

---

### Q8: "How did you handle CORS?"

**Answer:**
- **Problem**: CORS headers duplicated (Gateway + Services)
- **Solution**: Centralized CORS at API Gateway only
- **Implementation**: Reactive CorsWebFilter
- **Configuration**: 
  - Allowed Origin: `http://localhost:4200`
  - Allowed Methods: GET, POST, PUT, PATCH, DELETE, OPTIONS
  - Allowed Headers: All
  - Credentials: Enabled

```java
@Bean
public CorsWebFilter corsWebFilter() {
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowedOrigins(List.of("http://localhost:4200"));
    // ...
    return new CorsWebFilter(source);
}
```

---

### Q9: "Why H2 instead of PostgreSQL/MySQL?"

**Answer:**
- **Development Speed**: No external setup required
- **Portability**: Runs anywhere Java runs
- **Persistent**: File-based, not in-memory
- **H2 Console**: Built-in GUI for debugging
- **Production Path**: Easy to swap for PostgreSQL/MySQL (just change JDBC URL)
- **Database per Service**: 4 separate H2 databases

---

### Q10: "How would you scale this for production?"

**Answer:**

**Current State:**
- Single instance per service
- H2 databases
- Manual startup

**Production Enhancements:**

1. **Containerization**
   - Docker images for each service
   - Docker Compose for local orchestration

2. **Orchestration**
   - Kubernetes deployment
   - Horizontal pod autoscaling
   - Health checks and readiness probes

3. **Database**
   - Replace H2 with PostgreSQL/MySQL
   - Managed database services (AWS RDS)
   - Connection pooling (HikariCP)

4. **Caching**
   - Redis for session/token caching
   - Reduce database load

5. **Resilience**
   - Circuit breaker (Resilience4j)
   - Bulkhead pattern
   - Timeout configuration

6. **Monitoring**
   - Prometheus + Grafana
   - Distributed tracing (Zipkin/Jaeger)
   - ELK stack for logging

7. **CI/CD**
   - Jenkins/GitHub Actions pipeline
   - Automated testing
   - Blue-green deployments

8. **Security**
   - HTTPS/TLS everywhere
   - API rate limiting
   - Secret management (Vault)
   - OAuth2 instead of custom JWT

9. **Message Queue** (Future)
   - Kafka/RabbitMQ for async communication
   - Event-driven architecture

---

## 🐛 Challenges Faced & Solutions

### 1. CORS Duplicate Headers
**Problem**: Headers appeared twice (Gateway + Service)  
**Solution**: Removed CORS from services, centralized at Gateway

### 2. Service Discovery Issues
**Problem**: Services couldn't find each other  
**Solution**: Ensured Eureka starts first, services register properly

### 3. JWT Validation
**Problem**: Each service needs to validate independently  
**Solution**: Shared secret key across all services

### 4. Inter-Service Authentication
**Problem**: Should services pass JWT when calling each other?  
**Solution**: Internal APIs don't require JWT (services trust each other)

### 5. Cascade Deletion
**Problem**: Deleting customer should delete related data  
**Solution**: Orchestrated REST calls to all services

### 6. Maven Not Found
**Problem**: Platform-specific paths in tasks  
**Solution**: Made tasks.json cross-platform (no hardcoded paths)

---

## 📊 Data Flow Examples

### 1. User Registration (Customer)
```
Frontend → API Gateway → Users Service
                          ↓
                    Auth Service (create login)
                          ↓
                    Database (Customer + Login)
```

### 2. Login
```
Frontend → API Gateway → Auth Service
                          ↓
                    Validate credentials
                          ↓
                    Generate JWT
                          ↓
                    Return token to frontend
```

### 3. View Bills
```
Frontend (with JWT) → API Gateway → Billing Service
                                     ↓
                                Query bills by consumerId
                                     ↓
                                Return bill list
```

### 4. Pay Bill
```
Frontend → API Gateway → Payments Service
                          ↓
                    Validate bill exists (call Billing)
                          ↓
                    Create payment record
                          ↓
                    Update bill status (call Billing)
                          ↓
                    Return payment confirmation
```

### 5. Delete Customer (Cascade)
```
Frontend → API Gateway → Users Service
                          ↓
                    Call Auth (delete login)
                          ↓
                    Call Billing (delete bills)
                          ↓
                    Call Payments (delete payments)
                          ↓
                    Delete customer record
                          ↓
                    Rollback if any step fails
```

---

## 🚀 Quick Demo Script

**1. Show Architecture** (30 sec)
- Open Eureka: `http://localhost:8761`
- Point out 5 registered services

**2. Show Authentication** (1 min)
- Login as admin
- Show JWT in browser DevTools (Application → Session Storage)
- Show JWT in Network tab (Authorization header)

**3. Show CRUD Operations** (2 min)
- Create customer
- Create bill
- View bills (all + search)
- Pay bill
- Show status update

**4. Show Database Persistence** (1 min)
- Open H2 Console: `http://localhost:8082/h2-console`
- Show tables: `SELECT * FROM CUSTOMER`
- Restart service
- Show data still there

**5. Show Inter-Service Communication** (1 min)
- Delete customer
- Show cascade: Check H2 consoles - login, bills, payments all deleted

**6. Show Code** (2 min)
- API Gateway routes
- JWT validation
- RestTemplate with @LoadBalanced
- Service-to-service call

---

## 📝 Technical Details to Mention

### Spring Boot
- Version 3.2.0 (latest stable)
- Auto-configuration
- Embedded Tomcat
- Actuator for health checks

### Spring Cloud
- Version 2023.0.0
- Gateway (reactive)
- Eureka (service registry)
- LoadBalancer (client-side)

### Security
- JWT (JJWT library)
- BCrypt password hashing
- Role-based authorization
- HTTP-only session storage

### Database
- JPA/Hibernate
- H2 file-based
- Auto DDL (`ddl-auto: update`)
- Connection pooling

### Frontend
- Angular 18 (standalone components)
- TypeScript 5.4
- RxJS Observables
- HttpClient with interceptors

---

## 🎯 Key Metrics to Highlight

- **Lines of Code**: ~5,000+ (across all services)
- **Services**: 6 (4 business + 2 infrastructure)
- **Databases**: 4 (database per service)
- **API Endpoints**: 20+ REST endpoints
- **Components**: 12+ Angular components
- **Design Patterns**: 10+ patterns
- **Technologies**: 15+ technologies
- **Development Time**: [Your timeline]

---

## 💡 What Makes This Project Stand Out

1. ✅ **Complete Microservices** - Not just a monolith
2. ✅ **Service Discovery** - Dynamic service location
3. ✅ **API Gateway** - Production-ready pattern
4. ✅ **JWT Auth** - Custom implementation (not out-of-box)
5. ✅ **Inter-Service Calls** - Real distributed system
6. ✅ **Database per Service** - True microservices isolation
7. ✅ **Full Stack** - Backend + Frontend integration
8. ✅ **Modern Tech** - Latest Spring Boot & Angular
9. ✅ **Production Patterns** - Scalable architecture
10. ✅ **Documentation** - Comprehensive guides

---

## 🤔 Honest Answers

**"What would you do differently?"**
- Use Docker from the start
- Add unit/integration tests
- Implement circuit breaker explicitly
- Use message queue for async operations
- Add distributed tracing
- Use PostgreSQL instead of H2

**"What's missing?"**
- Automated tests
- Containerization
- CI/CD pipeline
- Monitoring/observability
- API versioning
- Rate limiting

**"What was the hardest part?"**
- Understanding service-to-service communication
- Debugging CORS issues
- Orchestrating cascade deletions
- Making it platform-independent

---

## 📚 Resources to Review Before Interview

1. Spring Cloud documentation
2. Netflix Eureka architecture
3. JWT specification
4. Microservices patterns (Martin Fowler)
5. RESTful API design best practices
6. Angular architecture guide
7. RxJS operators

---

**Good luck with your interview! 🚀**
