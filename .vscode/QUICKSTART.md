# ⚡ Electricity Billing System - Quick Start Guide

## 🎯 What Changed?

The backend was refactored from a **monolithic Spring Boot app** to **6 microservices**:

1. **Eureka Server** (port 8761) - Service discovery
2. **API Gateway** (port 8080) - Single entry point for frontend
3. **Auth Service** (port 8081) - Login, JWT, admin registration
4. **Users Service** (port 8082) - Customer CRUD, admin operations
5. **Billing Service** (port 8083) - Bill management
6. **Payments Service** (port 8084) - Payment processing

**Frontend**: No changes needed! Still uses `http://localhost:8080/api/*`

## 🚀 Start Everything (2 Steps)

### Step 1: Start Backend Services

Press **Cmd+Shift+P** → Type "**Tasks: Run Task**"

Run in this order (wait 10-15 seconds between each):
1. **Backend: 1. Eureka Server** ← Wait for "Started EurekaServerApplication"
2. **Backend: 2. API Gateway** ← Wait for "Started ApiGatewayApplication"
3. **Backend: 3. Auth Service**
4. **Backend: 4. Users Service**
5. **Backend: 5. Billing Service**
6. **Backend: 6. Payments Service**

**Tip**: Each opens its own terminal. Check Eureka at http://localhost:8761 to see all services registered.

### Step 2: Start Frontend

**Cmd+Shift+P** → "**Tasks: Run Task**" → **Frontend: Start Angular Dev Server**

Or manually:
```bash
cd frontend
npm start
```

## ✅ Verify Everything Works

1. **Eureka Dashboard**: http://localhost:8761
   - Should show 5 services: API-GATEWAY, AUTH-SERVICE, USERS-SERVICE, BILLING-SERVICE, PAYMENTS-SERVICE

2. **Frontend**: http://localhost:4200
   - Home page loads
   - Can login
   - No CORS errors in browser console (F12)

3. **API Gateway Health**: http://localhost:8080/api/health
   - Returns 200 OK

## 📁 Key Files Created

```
.vscode/
  ├── tasks.json                    # 7 tasks (6 backend + 1 frontend)
  ├── launch.json                   # Java debugger configs
  ├── QUICKSTART.md                 # This file
  ├── HOWTO-RUN-BACKEND.md          # Detailed backend instructions
  ├── START-FRONTEND.md             # Frontend instructions
  ├── FRONTEND-BACKEND-CONNECTION.md # Route mapping
  └── setup-java.sh                 # Set JAVA_HOME helper

backend/
  ├── pom.xml                       # Parent POM with Maven/Lombok config
  ├── eureka-server/                # Service discovery
  ├── api-gateway/                  # Entry point (CORS configured)
  ├── auth-service/                 # Login, JWT, admin register
  ├── users-service/                # Customers, admin operations
  ├── billing-service/              # Bills CRUD
  └── payments-service/             # Payment processing
```

## 🔧 Troubleshooting

**"mvn not found"**
- Already installed via `brew install maven`
- Tasks are pre-configured with Java 17

**"Connection refused" on startup**
- Normal! Services retry connecting to Eureka every 30 seconds
- Wait for "Started EurekaServerApplication" before starting other services

**CORS errors in browser**
- API Gateway has CORS configured for `http://localhost:4200`
- Restart API Gateway if you see CORS issues

**"argfile" error in Java debugger**
- Use Tasks instead of launch configs (more reliable)
- Tasks are the recommended way to run services

## 🧪 Test the System

1. **Register Admin**: http://localhost:4200/admin-register
2. **Login as Admin**: username/email from step 1
3. **Create Customer**: Admin dashboard → Create Consumer
4. **Login as Customer**: Use customer credentials
5. **View Bills**: Customer dashboard
6. **Pay Bill**: (if bills exist)

## 📚 More Info

- Backend architecture: `backend/README.md`
- Detailed backend startup: `.vscode/HOWTO-RUN-BACKEND.md`
- Frontend setup: `.vscode/START-FRONTEND.md`
- Route mapping: `.vscode/FRONTEND-BACKEND-CONNECTION.md`

## 💡 Pro Tips

- **Keep Eureka running** - Other services depend on it
- **Check Eureka dashboard** - Verify all services are registered
- **Watch terminal output** - Shows startup progress and errors
- **Use dedicated terminals** - Each task opens its own terminal for easy monitoring

---

**Need help?** Check the detailed guides in `.vscode/` folder or the terminal output for error messages.
