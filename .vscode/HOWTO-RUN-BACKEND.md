# How to Run the Backend

## Prerequisites

**1. Install Maven**

```bash
brew install maven
```

**2. Set JAVA_HOME to Java 17**

The project requires Java 17, but Maven may default to Java 25 (Homebrew). Add this to your `~/.zshrc`:

```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
```

Then reload:
```bash
source ~/.zshrc
```

Verify:
```bash
mvn -version
```

You should see **Java version: 17**.

## Running the backend

### Option 1: Use Tasks (easiest - already configured)

The tasks are pre-configured with Java 17, so they work out of the box:

1. Press **Cmd+Shift+P** (or Ctrl+Shift+P)
2. Type **"Tasks: Run Task"**
3. Select:
   - **Backend: 1. Eureka Server** (wait until it says "Started EurekaServerApplication")
   - **Backend: 2. API Gateway** (wait until it registers with Eureka)
   - **Backend: 3. Auth Service**
   - **Backend: 4. Users Service**
   - **Backend: 5. Billing Service**
   - **Backend: 6. Payments Service**

Each task opens a dedicated terminal and runs `mvn spring-boot:run` with Java 17.

### Option 2: Use terminals manually

Open 6 terminals and run:

```bash
# Terminal 1
cd backend/eureka-server && export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home && mvn spring-boot:run

# Terminal 2 (after Eureka is up)
cd backend/api-gateway && export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home && mvn spring-boot:run

# Terminal 3-6 (same pattern)
cd backend/auth-service && export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home && mvn spring-boot:run
# ... etc
```

Or set JAVA_HOME permanently by running:
```bash
./.vscode/setup-java.sh
source ~/.zshrc
```

### Option 3: Run from your IDE (IntelliJ/Cursor)

If you have IntelliJ IDEA or the Java extension configured:

1. Open the main class (e.g. `EurekaServerApplication.java`)
2. Right-click → **Run** (or click the green ▶ icon)

Start **Eureka** first, then **API Gateway**, then the rest.

## Check it works

- **Eureka**: http://localhost:8761
- **API Gateway**: http://localhost:8080/api/health

Once all 6 services are up, you should see all of them registered in the Eureka dashboard.

## Frontend

The frontend continues to use **http://localhost:8080** (the API Gateway). No changes needed to the frontend.
