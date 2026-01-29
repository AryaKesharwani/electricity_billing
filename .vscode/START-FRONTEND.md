# How to Start the Frontend

## Prerequisites

Make sure Node.js and npm are installed:
```bash
node --version  # Should be v18 or higher
npm --version
```

## Install Dependencies (First Time Only)

```bash
cd frontend
npm install
```

## Start the Frontend

```bash
cd frontend
npm start
```

The Angular dev server will start on **http://localhost:4200**

## Connect to Backend

The frontend is pre-configured to connect to the API Gateway at **http://localhost:8080/api/**

Make sure the backend services are running:
1. Eureka Server (port 8761)
2. API Gateway (port 8080)
3. Auth Service (port 8081)
4. Users Service (port 8082)
5. Billing Service (port 8083)
6. Payments Service (port 8084)

See `.vscode/HOWTO-RUN-BACKEND.md` for backend startup instructions.

## Frontend Routes

- `/` - Home page
- `/login` - Login page
- `/register` - Customer registration
- `/admin-register` - Admin registration
- `/customer-dashboard` - Customer dashboard (requires login)
- `/admin-dashboard` - Admin dashboard (requires login)
- `/view-bills` - View bills
- `/create-bill` - Create new bill (admin only)
- `/pay-bill` - Pay bill (customer only)

## Troubleshooting

**CORS errors**: Make sure the API Gateway is running with CORS configuration enabled for `http://localhost:4200`

**Authentication errors**: Check that the auth-service is running and the JWT secret is consistent across services

**404 errors**: Verify the API Gateway routes are configured and all backend services are registered with Eureka
