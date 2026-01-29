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

## ⚙️ Platform Support

This project now works on **Windows**, **macOS**, and **Linux**! 

**First time setup?** See `.vscode/PLATFORM-SETUP.md` for platform-specific installation instructions.

## 🚀 Start Everything (2 Steps)

### Prerequisites

Make sure you have:
- ✅ **Java 17** installed and `JAVA_HOME` set
- ✅ **Maven** installed
- ✅ **Node.js** (v18+) installed

Verify with:
```bash
java -version    # Should show version 17
mvn -version     # Should show Maven 3.x with Java 17
node --version   # Should show v18 or higher
```

**Need help?** See `.vscode/PLATFORM-SETUP.md`

### Step 1: Start Backend Services

Press **Cmd+Shift+P** (macOS) or **Ctrl+Shift+P** (Windows/Linux) → Type "**Tasks: Run Task**"

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

4. **H2 Database Consoles** (optional):
   - Auth: http://localhost:8081/h2-console
   - Users: http://localhost:8082/h2-console
   - Billing: http://localhost:8083/h2-console
   - Payments: http://localhost:8084/h2-console
   - See `.vscode/H2-CONSOLE-ACCESS.md` for login details

## 📁 Key Files Created

```
.vscode/
  ├── tasks.json                    # 7 tasks (6 backend + 1 frontend)
  ├── launch.json                   # Java debugger configs
  ├── QUICKSTART.md                 # This file
  ├── HOWTO-RUN-BACKEND.md          # Detailed backend instructions
  ├── START-FRONTEND.md             # Frontend instructions
  ├── FRONTEND-BACKEND-CONNECTION.md # Route mapping
  ├── PLATFORM-SETUP.md             # Cross-platform setup guide
  ├── H2-CONSOLE-ACCESS.md          # Database console access
  └── setup-java.sh                 # Set JAVA_HOME helper

backend/
  ├── pom.xml                       # Parent POM with Maven/Lombok config
  ├── DATABASE.md                   # Database documentation
  ├── eureka-server/                # Service discovery
  ├── api-gateway/                  # Entry point (CORS configured)
  ├── auth-service/                 # Login, JWT, admin register
  │   └── data/                     # Persistent H2 database
  ├── users-service/                # Customers, admin operations
  │   └── data/                     # Persistent H2 database
  ├── billing-service/              # Bills CRUD
  │   └── data/                     # Persistent H2 database
  └── payments-service/             # Payment processing
      └── data/                     # Persistent H2 database
```

## 🔧 Troubleshooting

**"mvn not found" or "java not found"**
- Make sure Maven and Java 17 are installed
- Verify they're in your PATH: `mvn -version` and `java -version`
- See `.vscode/PLATFORM-SETUP.md` for installation instructions

**"Unsupported class file major version"**
- Your JAVA_HOME is not pointing to Java 17
- Set JAVA_HOME to Java 17 (see PLATFORM-SETUP.md)
- Verify with: `mvn -version` (should show Java 17)

**"Connection refused" on startup**
- Normal! Services retry connecting to Eureka every 30 seconds
- Wait for "Started EurekaServerApplication" before starting other services

**CORS errors in browser**
- API Gateway has CORS configured for `http://localhost:4200`
- Restart API Gateway if you see CORS issues

**Port already in use**
- A service is already running on that port
- Windows: `netstat -ano | findstr :8080` then `taskkill /PID <PID> /F`
- macOS/Linux: `lsof -ti:8080 | xargs kill -9`

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
- **Data persists!** - All your data (users, bills, payments) survives service restarts
- **H2 Console** - Use the web interface to view/debug database data
- **Backup data** - See `backend/DATABASE.md` for backup procedures

---

**Need help?** Check the detailed guides in `.vscode/` folder or the terminal output for error messages.
