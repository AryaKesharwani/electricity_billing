# ⚡ Electricity Billing System

> A production-ready, full-stack microservices application for electricity billing management

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-18.0-red)](https://angular.io/)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-Educational-blue)](LICENSE)

---

## 🚀 Quick Links

- **[Interview README](./INTERVIEW-README.md)** - Complete technical documentation for interviews
- **[Interview Cheat Sheet](./INTERVIEW-CHEATSHEET.md)** - Quick reference Q&A for interviews
- **[Quick Start Guide](./.vscode/QUICKSTART.md)** - Get started in 5 minutes
- **[Platform Setup](./.vscode/PLATFORM-SETUP.md)** - Windows/macOS/Linux setup
- **[Database Documentation](./backend/DATABASE.md)** - H2 database guide

---

## 📋 Overview

A comprehensive **microservices-based** electricity billing management system featuring:

- 🏗️ **6 Microservices** (Eureka, Gateway, Auth, Users, Billing, Payments)
- 🔐 **JWT Authentication** with role-based access control
- 🌐 **API Gateway** for centralized routing
- 🔍 **Service Discovery** using Netflix Eureka
- 💾 **Persistent H2 Databases** (database per service)
- 🎨 **Modern Angular SPA** with reactive programming

---

## 🛠️ Technology Stack

### Backend
- Java 17, Spring Boot 3.2.0
- Spring Cloud (Gateway, Eureka)
- Spring Data JPA, Hibernate
- JWT Authentication, H2 Database
- Maven, Lombok

### Frontend
- Angular 18, TypeScript 5.4
- RxJS, HTTP Interceptors
- Route Guards, Reactive Forms

---

## 🏗️ Architecture

```
Frontend (Angular)
       ↓
API Gateway :8080
       ↓
┌──────┴──────┬─────────┬──────────┐
↓             ↓         ↓          ↓
Auth :8081  Users    Billing   Payments
           :8082     :8083      :8084
```

All services register with **Eureka :8761** for service discovery.

See [INTERVIEW-README.md](./INTERVIEW-README.md) for detailed architecture.

---

## ⚡ Quick Start

### Prerequisites
- Java 17+
- Maven 3.9+
- Node.js 18+
- npm 10+

### 1. Build Backend
```bash
cd backend
mvn clean install
```

### 2. Start Services (Use VS Code Tasks)
1. Press **Cmd+Shift+P** (or Ctrl+Shift+P)
2. Type **"Tasks: Run Task"**
3. Run in order:
   - Backend: 1. Eureka Server
   - Backend: 2. API Gateway
   - Backend: 3-6. Auth, Users, Billing, Payments

### 3. Start Frontend
```bash
cd frontend
npm install  # First time only
npm start
```

**Access:**
- Frontend: http://localhost:4200
- Eureka: http://localhost:8761
- API Gateway: http://localhost:8080

See [QUICKSTART.md](./.vscode/QUICKSTART.md) for detailed instructions.

---

## 📚 Documentation

### For Interviews
- **[INTERVIEW-README.md](./INTERVIEW-README.md)** - Complete technical overview
- **[INTERVIEW-CHEATSHEET.md](./INTERVIEW-CHEATSHEET.md)** - Q&A preparation

### For Development
- **[QUICKSTART.md](./.vscode/QUICKSTART.md)** - Get started quickly
- **[PLATFORM-SETUP.md](./.vscode/PLATFORM-SETUP.md)** - Cross-platform setup
- **[HOWTO-RUN-BACKEND.md](./.vscode/HOWTO-RUN-BACKEND.md)** - Backend details
- **[DATABASE.md](./backend/DATABASE.md)** - Database documentation
- **[H2-CONSOLE-ACCESS.md](./.vscode/H2-CONSOLE-ACCESS.md)** - H2 console guide

### Architecture
- **[backend/README.md](./backend/README.md)** - Microservices overview
- **[FRONTEND-BACKEND-CONNECTION.md](./.vscode/FRONTEND-BACKEND-CONNECTION.md)** - API route mapping

---

## ✨ Key Features

### User Management
- Admin and customer registration
- JWT-based authentication
- Role-based access control
- Customer CRUD operations

### Billing
- Create bills for customers
- View all bills (admin) or personal bills (customer)
- Update bill status (PAID/UNPAID/OVERDUE)
- Search bills by consumer ID

### Payments
- Pay bills online
- Payment history tracking
- Automatic bill status update

### Technical
- Service discovery with Eureka
- API Gateway routing
- Inter-service communication
- Database per service
- Persistent data storage
- H2 web console for debugging

---

## 🎯 Project Structure

```
electricity_billing/
├── backend/
│   ├── eureka-server/          # Service discovery (8761)
│   ├── api-gateway/            # API Gateway (8080)
│   ├── auth-service/           # Authentication (8081)
│   ├── users-service/          # Customer management (8082)
│   ├── billing-service/        # Bill operations (8083)
│   ├── payments-service/       # Payment processing (8084)
│   └── DATABASE.md             # Database documentation
├── frontend/
│   └── src/app/
│       ├── services/           # API services
│       ├── models/             # TypeScript models
│       ├── guards/             # Route guards
│       ├── interceptors/       # HTTP interceptors
│       └── [components]/       # UI components
├── .vscode/
│   ├── tasks.json              # VS Code tasks (platform-independent)
│   ├── QUICKSTART.md           # Quick start guide
│   ├── PLATFORM-SETUP.md       # Setup for all platforms
│   └── [other guides]/         # Additional documentation
├── INTERVIEW-README.md         # Technical documentation
├── INTERVIEW-CHEATSHEET.md     # Interview Q&A
└── README.md                   # This file
```

---

## 🔑 API Endpoints

### Authentication
- `POST /api/auth/validateLogin` - User login
- `POST /api/admin/register` - Admin registration

### Customers
- `POST /api/customers/register` - Customer registration
- `GET /api/admin/customers` - List all customers
- `POST /api/admin/customers` - Create customer (admin)
- `DELETE /api/admin/customers/{id}` - Delete customer

### Bills
- `GET /api/bills` - Get all bills
- `GET /api/bills/viewBills?consumerId={id}` - Get bills by consumer
- `POST /api/bills` - Create bill (admin)
- `PATCH /api/bills/{id}/status` - Update bill status

### Payments
- `POST /api/payments/payBills` - Pay a bill

See [FRONTEND-BACKEND-CONNECTION.md](./.vscode/FRONTEND-BACKEND-CONNECTION.md) for complete API mapping.

---

## 💾 Database Access

Each service has its own H2 database with web console:

| Service | Console URL | JDBC URL |
|---------|------------|----------|
| Auth | http://localhost:8081/h2-console | `jdbc:h2:file:./data/authdb` |
| Users | http://localhost:8082/h2-console | `jdbc:h2:file:./data/usersdb` |
| Billing | http://localhost:8083/h2-console | `jdbc:h2:file:./data/billingdb` |
| Payments | http://localhost:8084/h2-console | `jdbc:h2:file:./data/paymentsdb` |

**Login:** Username `sa`, Password *(empty)*

See [H2-CONSOLE-ACCESS.md](./.vscode/H2-CONSOLE-ACCESS.md) for details.

---

## 🎓 What This Project Demonstrates

### Architecture
✅ Microservices design  
✅ Service discovery (Eureka)  
✅ API Gateway pattern  
✅ Database per service  
✅ Inter-service communication  

### Backend
✅ Spring Boot + Spring Cloud  
✅ RESTful API design  
✅ JWT authentication  
✅ JPA/Hibernate ORM  
✅ Exception handling  

### Frontend
✅ Angular 18 (standalone components)  
✅ Reactive programming (RxJS)  
✅ HTTP interceptors  
✅ Route guards  
✅ TypeScript models  

### DevOps
✅ Maven multi-module build  
✅ VS Code tasks (cross-platform)  
✅ Git version control  
✅ Comprehensive documentation  

---

## 🚀 Production Roadmap

### Current State
- ✅ Microservices architecture
- ✅ Persistent databases
- ✅ JWT authentication
- ✅ Service discovery

### Future Enhancements
- 🔲 Docker containerization
- 🔲 Kubernetes orchestration
- 🔲 PostgreSQL/MySQL migration
- 🔲 Redis caching
- 🔲 Circuit breaker (Resilience4j)
- 🔲 Distributed tracing (Zipkin)
- 🔲 Monitoring (Prometheus + Grafana)
- 🔲 CI/CD pipeline
- 🔲 Unit & integration tests
- 🔲 Message queue (Kafka/RabbitMQ)

---

## 🐛 Troubleshooting

**Services not starting?**
- Check Java 17 is installed: `java -version`
- Check Maven is installed: `mvn -version`
- See [PLATFORM-SETUP.md](./.vscode/PLATFORM-SETUP.md)

**Frontend errors?**
- Check Node.js version: `node --version` (need 18+)
- Run `npm install` in frontend directory

**CORS errors?**
- Restart API Gateway service
- Check API Gateway is on port 8080

**Database errors?**
- Check `data/` directory exists in each service
- See [DATABASE.md](./backend/DATABASE.md)

---

## 📖 Learning Resources

This project was built using:
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Angular Documentation](https://angular.io/docs)
- [Netflix Eureka Wiki](https://github.com/Netflix/eureka/wiki)
- [JWT Introduction](https://jwt.io/introduction)

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

## 📞 Contact

For questions or feedback, please reach out via:
- GitHub Issues
- Email: [Your Email]
- LinkedIn: [Your LinkedIn]

---

**⭐ Star this repo if you find it useful for learning microservices!**

**Last Updated:** January 2026

