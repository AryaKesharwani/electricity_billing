# Electricity Billing System

A full-stack application for managing electricity billing with Angular frontend and Spring Boot backend.

## Project Structure

```
electricity_billing/
├── frontend/          # Angular application
└── backend/           # Spring Boot application
```

## Getting Started

### Prerequisites

- Node.js (v20.19+ or v22.12+)
- npm or yarn
- Java 17 or higher
- Maven 3.6+

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
# or
ng serve
```

The frontend will be available at `http://localhost:4200`

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The backend API will be available at `http://localhost:8080`

## API Endpoints

- `GET /api/health` - Health check endpoint

## Development

The backend is configured to accept CORS requests from `http://localhost:4200` for development purposes.

The backend uses H2 in-memory database for development. You can access the H2 console at `http://localhost:8080/h2-console` with:
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

