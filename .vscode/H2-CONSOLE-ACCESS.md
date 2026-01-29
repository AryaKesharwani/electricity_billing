# H2 Database Console Access

## Quick Access URLs

Each microservice has its own H2 web console for viewing and managing data:

| Service | H2 Console URL | Port |
|---------|---------------|------|
| **Auth Service** | http://localhost:8081/h2-console | 8081 |
| **Users Service** | http://localhost:8082/h2-console | 8082 |
| **Billing Service** | http://localhost:8083/h2-console | 8083 |
| **Payments Service** | http://localhost:8084/h2-console | 8084 |

## Login Credentials

When you open any H2 console, use these settings:

```
Saved Settings: Generic H2 (Embedded)
Setting Name: Generic H2 (Embedded)
Driver Class: org.h2.Driver
JDBC URL: (see table below)
User Name: sa
Password: (leave empty)
```

### JDBC URLs

| Service | JDBC URL |
|---------|----------|
| Auth Service | `jdbc:h2:file:./data/authdb` |
| Users Service | `jdbc:h2:file:./data/usersdb` |
| Billing Service | `jdbc:h2:file:./data/billingdb` |
| Payments Service | `jdbc:h2:file:./data/paymentsdb` |

## What You Can Do

### View Data

```sql
-- See all tables
SHOW TABLES;

-- View login credentials (auth-service)
SELECT * FROM LOGIN;

-- View customers (users-service)
SELECT * FROM CUSTOMER;

-- View bills (billing-service)
SELECT * FROM BILL;

-- View payments (payments-service)
SELECT * FROM PAYMENT;
```

### Inspect Schema

```sql
-- Show table structure
SHOW COLUMNS FROM LOGIN;
SHOW COLUMNS FROM CUSTOMER;
SHOW COLUMNS FROM BILL;
SHOW COLUMNS FROM PAYMENT;
```

### Query Data

```sql
-- Find a specific user
SELECT * FROM LOGIN WHERE EMAIL = 'admin@example.com';

-- Count customers
SELECT COUNT(*) FROM CUSTOMER;

-- View unpaid bills
SELECT * FROM BILL WHERE STATUS = 'UNPAID';

-- View recent payments
SELECT * FROM PAYMENT ORDER BY PAYMENT_DATE DESC LIMIT 10;
```

### Join Across Services (NOT RECOMMENDED)

**Note**: In microservices, you should NOT join across databases. Each service manages its own data. If you need related data from multiple services, use the REST APIs.

However, for debugging, you can query each database separately:

1. Auth Service: Get login info
2. Users Service: Get customer details using consumer_id
3. Billing Service: Get bills for that consumer_id
4. Payments Service: Get payments for those bill_id values

## Data Persistence

✅ **Data is now persistent!**

- All data saves to disk in each service's `data/` directory
- Data survives service restarts
- Database files: `backend/<service>/data/<dbname>.mv.db`

**Before (in-memory):**
```
jdbc:h2:mem:authdb  ← Lost on restart
```

**After (file-based):**
```
jdbc:h2:file:./data/authdb  ← Persists to disk
```

## Backup & Reset

### Backup All Databases

```bash
cd backend
mkdir -p backups/$(date +%Y%m%d)
cp -r */data backups/$(date +%Y%m%d)/
```

### Reset All Data (Fresh Start)

```bash
# Stop all services first!
cd backend
rm -rf auth-service/data
rm -rf users-service/data
rm -rf billing-service/data
rm -rf payments-service/data
```

### Export Specific Table

In H2 Console, run:
```sql
-- Export to CSV
CALL CSVWRITE('export.csv', 'SELECT * FROM CUSTOMER');
```

### Import Data

```sql
-- Import from CSV
CREATE TABLE TEMP AS SELECT * FROM CSVREAD('export.csv');
INSERT INTO CUSTOMER SELECT * FROM TEMP;
DROP TABLE TEMP;
```

## Troubleshooting

### Cannot Connect to H2 Console

**Problem**: Page doesn't load

**Solution**:
- Verify the service is running
- Check the correct port (8081, 8082, 8083, or 8084)
- Ensure you're using `http://` not `https://`

### Login Failed

**Problem**: Wrong username/password

**Solution**:
- Username: `sa` (lowercase)
- Password: (leave empty - press "Connect" without entering a password)

### Wrong JDBC URL

**Problem**: "Database not found" or "Connection refused"

**Solution**:
- Copy the exact JDBC URL from the table above
- Make sure it matches: `jdbc:h2:file:./data/<dbname>`
- Don't use `jdbc:h2:mem:` (that's in-memory, not persistent)

### Tables Not Found

**Problem**: "Table not found" error

**Solution**:
- The service might not have started yet (tables created on first start)
- Check if `data/` directory exists in the service folder
- Verify Hibernate created the tables (check service logs for `ddl-auto: update`)

### Database Locked

**Problem**: "Database may be already in use"

**Solution**:
- Another instance of the service is running
- Stop all instances: `ps aux | grep spring-boot | grep <service-name>`
- Kill the process: `kill <PID>`

## Security Warning

⚠️ **For Development Only**

The H2 console is **enabled by default** for development convenience.

**For Production**:
- Set `spring.h2.console.enabled: false`
- Use a production database (PostgreSQL, MySQL)
- Never expose H2 console to the internet
- Change default passwords

See `backend/DATABASE.md` for production considerations.
