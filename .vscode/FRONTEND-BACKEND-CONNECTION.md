# Frontend-Backend Connection Map

## ✅ Connection Status: **READY**

The frontend is **already configured** to work with the microservices backend through the API Gateway. No changes were needed!

## Architecture Overview

```
Frontend (Angular on :4200)
         ↓
API Gateway (:8080) ← Eureka Service Discovery (:8761)
         ↓
    ┌────┴────┬─────────┬──────────┬─────────────┐
    ↓         ↓         ↓          ↓             ↓
Auth-Service Users    Billing  Payments      (More services)
  (:8081)   (:8082)   (:8083)   (:8084)
```

## Frontend → Backend Route Mapping

### 1. Authentication (`auth.service.ts`)
| Frontend Call | API Gateway Route | Microservice | Port |
|--------------|-------------------|--------------|------|
| `POST /api/auth/validateLogin` | `/api/auth/**` | auth-service | 8081 |

### 2. Admin Operations (`admin.service.ts`)
| Frontend Call | API Gateway Route | Microservice | Port |
|--------------|-------------------|--------------|------|
| `POST /api/admin/register` | `/api/admin/register` | auth-service | 8081 |
| `GET /api/admin/customers` | `/api/admin/**` | users-service | 8082 |
| `POST /api/admin/customers` | `/api/admin/**` | users-service | 8082 |
| `DELETE /api/admin/customers/{id}` | `/api/admin/**` | users-service | 8082 |

### 3. Customer Operations (`customer.service.ts`)
| Frontend Call | API Gateway Route | Microservice | Port |
|--------------|-------------------|--------------|------|
| `POST /api/customers/register` | `/api/customers/**` | users-service | 8082 |

### 4. Billing Operations (`bill.service.ts`)
| Frontend Call | API Gateway Route | Microservice | Port |
|--------------|-------------------|--------------|------|
| `GET /api/bills/viewBills?consumerId={id}` | `/api/bills/**` | billing-service | 8083 |
| `GET /api/bills` | `/api/bills/**` | billing-service | 8083 |
| `POST /api/bills` | `/api/bills/**` | billing-service | 8083 |
| `PATCH /api/bills/{id}/status` | `/api/bills/**` | billing-service | 8083 |

### 5. Payment Operations (`payment.service.ts`)
| Frontend Call | API Gateway Route | Microservice | Port |
|--------------|-------------------|--------------|------|
| `POST /api/payments/payBills` | `/api/payments/**` | payments-service | 8084 |

## CORS Configuration

The API Gateway is configured to accept requests from:
- **Frontend Origin**: `http://localhost:4200`
- **Allowed Methods**: GET, POST, PUT, PATCH, DELETE, OPTIONS
- **Allowed Headers**: All (`*`)
- **Credentials**: Enabled (for JWT tokens)

## JWT Authentication Flow

1. **Frontend** sends login request to `/api/auth/validateLogin`
2. **API Gateway** routes to **auth-service**
3. **auth-service** validates credentials and returns JWT token
4. **Frontend** stores JWT in AuthService
5. **HTTP Interceptor** (`auth.interceptor.ts`) adds JWT to all subsequent requests
6. **API Gateway** forwards requests with JWT to appropriate microservices
7. **Microservices** validate JWT using shared secret

## Starting the Full Stack

### 1. Start Backend (in order):
```bash
# Via VS Code Tasks (Cmd+Shift+P → "Tasks: Run Task")
1. Backend: 1. Eureka Server
2. Backend: 2. API Gateway (wait for Eureka)
3. Backend: 3. Auth Service
4. Backend: 4. Users Service
5. Backend: 5. Billing Service
6. Backend: 6. Payments Service
```

### 2. Start Frontend:
```bash
# Via VS Code Task
Frontend: Start Angular Dev Server

# Or manually:
cd frontend && npm start
```

### 3. Access the Application:
- **Frontend**: http://localhost:4200
- **Eureka Dashboard**: http://localhost:8761
- **API Gateway Health**: http://localhost:8080/api/health

## Verification Checklist

- [ ] Eureka Server running on :8761
- [ ] All 5 services registered with Eureka
- [ ] API Gateway running on :8080
- [ ] Frontend running on :4200
- [ ] No CORS errors in browser console
- [ ] Can login and see JWT token in browser DevTools
- [ ] Can navigate between pages
- [ ] API calls succeed (check Network tab)

## Troubleshooting

**CORS Errors**
- Verify API Gateway is running
- Check browser console for exact error
- Confirm CORS config in `api-gateway/config/CorsConfig.java`

**404 Not Found**
- Check service is registered in Eureka (:8761)
- Verify route exists in `api-gateway/application.yml`
- Ensure service name matches in Eureka and route config

**JWT Authentication Fails**
- Verify all services use same JWT secret
- Check token in browser DevTools (Application → Local Storage)
- Confirm interceptor is adding Authorization header (Network tab)

**Connection Refused**
- Service not started or crashed
- Check terminal for error logs
- Restart service and check Eureka registration
