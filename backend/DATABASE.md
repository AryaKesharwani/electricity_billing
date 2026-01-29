# Database Configuration

## Overview

Each microservice uses its own **persistent H2 database** stored as files in the `data/` directory within each service.

## Database Locations

```
backend/
├── auth-service/data/authdb.mv.db          # Login credentials, admin data
├── users-service/data/usersdb.mv.db        # Customer information
├── billing-service/data/billingdb.mv.db    # Bills and billing records
└── payments-service/data/paymentsdb.mv.db  # Payment transactions
```

## Persistence

- **Before**: Data was stored in-memory and lost on restart
- **After**: Data persists to disk and survives restarts

The databases are created automatically when each service starts for the first time.

## H2 Console Access

Each service has the H2 web console enabled for debugging and data inspection:

| Service | Console URL | JDBC URL |
|---------|------------|----------|
| Auth Service | http://localhost:8081/h2-console | jdbc:h2:file:./data/authdb |
| Users Service | http://localhost:8082/h2-console | jdbc:h2:file:./data/usersdb |
| Billing Service | http://localhost:8083/h2-console | jdbc:h2:file:./data/billingdb |
| Payments Service | http://localhost:8084/h2-console | jdbc:h2:file:./data/paymentsdb |

**Login credentials:**
- **Username**: `sa`
- **Password**: *(leave empty)*

## Managing Data

### Backup Data

To backup all databases:

```bash
# From backend/ directory
mkdir -p backups
cp -r auth-service/data backups/auth-$(date +%Y%m%d)
cp -r users-service/data backups/users-$(date +%Y%m%d)
cp -r billing-service/data backups/billing-$(date +%Y%m%d)
cp -r payments-service/data backups/payments-$(date +%Y%m%d)
```

### Reset All Data

To start fresh (delete all data):

```bash
# Stop all services first, then:
cd backend
rm -rf */data/
```

When services restart, they'll create fresh empty databases.

### Reset Single Service

To reset just one service's data:

```bash
# Stop the service first, then:
cd backend/auth-service   # or users-service, billing-service, payments-service
rm -rf data/
```

## Production Considerations

For production deployments, consider:

1. **Switch to PostgreSQL/MySQL**
   - H2 is suitable for development/testing
   - Production should use a robust RDBMS

2. **Backup Strategy**
   - Regular automated backups
   - Test restore procedures

3. **Database per Service**
   - Each microservice has its own database (as designed)
   - Services communicate via REST APIs, not direct database access

4. **Security**
   - Change default H2 passwords
   - Disable H2 console in production
   - Use encrypted connections

## Configuration Details

Database configuration is in each service's `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:h2:file:./data/dbname;AUTO_SERVER=TRUE
    driverClassName: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    hibernate:
      ddl-auto: update
```

**Key settings:**
- `file:./data/dbname` - Persist to file instead of memory
- `AUTO_SERVER=TRUE` - Allow multiple connections
- `ddl-auto: update` - Auto-create/update tables from JPA entities
- `console.enabled: true` - Enable H2 web console

## Troubleshooting

**"Database locked" error**
- Another process has the database file open
- Stop all instances of the service
- Check for zombie processes: `ps aux | grep spring-boot`

**Data not persisting**
- Check `application.yml` has `jdbc:h2:file:` not `jdbc:h2:mem:`
- Verify `data/` directory exists and is writable
- Check logs for database errors

**Cannot connect to H2 console**
- Ensure service is running
- Use correct JDBC URL (must match application.yml)
- Username is `sa`, password is empty

**Database corruption**
- Backup corrupted database first
- Delete `.mv.db` file
- Restart service to create fresh database
- Restore from backup if needed
