# Platform-Independent Setup Guide

This guide explains how to set up the environment on **Windows**, **macOS**, or **Linux**.

## Prerequisites

### 1. Install Java 17

**Windows:**
```powershell
# Using Chocolatey
choco install openjdk17

# Or download from: https://adoptium.net/
```

**macOS:**
```bash
# Using Homebrew
brew install openjdk@17
```

**Linux:**
```bash
# Ubuntu/Debian
sudo apt install openjdk-17-jdk

# Fedora/RHEL
sudo dnf install java-17-openjdk-devel
```

### 2. Install Maven

**Windows:**
```powershell
# Using Chocolatey
choco install maven

# Or download from: https://maven.apache.org/download.cgi
```

**macOS:**
```bash
# Using Homebrew
brew install maven
```

**Linux:**
```bash
# Ubuntu/Debian
sudo apt install maven

# Fedora/RHEL
sudo dnf install maven
```

### 3. Install Node.js (v18 or higher)

**Windows:**
```powershell
# Using Chocolatey
choco install nodejs-lts

# Or download from: https://nodejs.org/
```

**macOS:**
```bash
# Using Homebrew
brew install node@20
```

**Linux:**
```bash
# Ubuntu/Debian
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt install -y nodejs

# Fedora/RHEL
sudo dnf install nodejs npm
```

## Environment Configuration

### Set JAVA_HOME to Java 17

**Windows (PowerShell):**
```powershell
# Find Java 17 installation
Get-Command java | Select-Object -ExpandProperty Definition

# Set JAVA_HOME (adjust path to your Java 17 installation)
[System.Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Java\jdk-17', 'User')

# Or temporarily in current session:
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
```

**Windows (Command Prompt):**
```cmd
REM Set JAVA_HOME permanently
setx JAVA_HOME "C:\Program Files\Java\jdk-17"

REM Or temporarily in current session:
set JAVA_HOME=C:\Program Files\Java\jdk-17
```

**macOS/Linux (bash/zsh):**
```bash
# Add to ~/.bashrc, ~/.zshrc, or ~/.profile
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home  # macOS
# OR
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64  # Linux Ubuntu/Debian
# OR
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk  # Linux Fedora/RHEL

# Then reload:
source ~/.zshrc  # or ~/.bashrc or ~/.profile
```

### Verify Installation

Run these commands to verify everything is set up correctly:

```bash
# Check Java version (should be 17)
java -version

# Check Maven
mvn -version

# Check Node.js
node --version

# Check npm
npm --version

# Verify JAVA_HOME
echo $JAVA_HOME          # macOS/Linux
echo %JAVA_HOME%         # Windows CMD
echo $env:JAVA_HOME      # Windows PowerShell
```

## Running the Application

Once the environment is configured, the VS Code tasks will work on **any platform**:

1. **Cmd+Shift+P** (macOS) or **Ctrl+Shift+P** (Windows/Linux)
2. Type **"Tasks: Run Task"**
3. Select the service to start

The tasks are now platform-independent and use your system's:
- Default shell (PowerShell on Windows, bash/zsh on macOS/Linux)
- Maven from PATH
- Java from JAVA_HOME

## Troubleshooting

### "mvn not found" or "java not found"

**Cause:** Maven or Java not in system PATH

**Fix:**
- Restart your terminal/VS Code after installation
- Verify PATH includes Maven and Java bin directories
- On Windows, you may need to restart your computer

### "Unsupported class file major version" or "Java version mismatch"

**Cause:** JAVA_HOME points to wrong Java version

**Fix:**
- Verify `mvn -version` shows Java 17
- Set JAVA_HOME to Java 17 installation (see above)
- Restart terminal/VS Code

### Tasks fail on Windows

**Cause:** Shell configuration or path separators

**Fix:**
- The new tasks.json is platform-agnostic
- Make sure VS Code is using your default shell
- Check File → Preferences → Settings → Terminal: Integrated Shell

### Port already in use

**Cause:** Previous service instance still running

**Fix:**
```bash
# Find and kill process on specific port (e.g., 8080)

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# macOS/Linux
lsof -ti:8080 | xargs kill -9
```

## Next Steps

- See `.vscode/QUICKSTART.md` for quick start instructions
- See `.vscode/HOWTO-RUN-BACKEND.md` for detailed backend setup
- See `.vscode/START-FRONTEND.md` for frontend setup
