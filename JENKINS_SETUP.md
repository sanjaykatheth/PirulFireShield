# Jenkins CI/CD Integration Guide for PirulFireShield

This guide explains how to set up Jenkins to build, test, and deploy your Spring Boot application.

## Prerequisites

1. **Jenkins Installation**
   - Install Jenkins on your server or use Jenkins in Docker
   - Required plugins: Pipeline, Git, Maven Integration, Docker Pipeline

2. **Docker** (optional, for containerization)
   - Install Docker on the Jenkins server
   - For Windows: [Docker Desktop](https://www.docker.com/products/docker-desktop)

## Jenkins Setup Steps

### Step 1: Install Required Jenkins Plugins

Navigate to **Manage Jenkins > Manage Plugins** and install:

- Pipeline
- Git
- Maven Integration
- Docker Pipeline
- Email Extension

### Step 2: Configure Maven

1. Go to **Manage Jenkins > Global Tool Configuration**
2. Add Maven installation:
   - Name: `Maven-3.9`
   - Version: 3.9.x or latest

### Step 3: Create Jenkins Pipeline Job

1. Click **New Item** > Select **Pipeline**
2. Configure:
   - **General**: Check "This project is parameterized"
     - Add String Parameter: `DOCKER_IMAGE` (default: `pirul-fireshield`)
   - **Pipeline**: Select "Pipeline script from SCM"
     - SCM: Git
     - Repository URL: Your repository URL
     - Branch: `*/main` or `*/master`
     - Script Path: `Jenkinsfile`

### Step 4: Configure Credentials

Add credentials for:

- Git repository (username/password or SSH key)
- Docker registry (if using private registry)
- Database (if needed for tests)

## Pipeline Stages Explained

The [`Jenkinsfile`](Jenkinsfile) includes:

| Stage                  | Description                             |
| ---------------------- | --------------------------------------- |
| **Checkout**           | Pulls source code from Git repository   |
| **Build**              | Compiles the Maven project              |
| **Test**               | Runs unit tests with JUnit              |
| **Package**            | Creates JAR file                        |
| **Build Docker Image** | Creates Docker image (main branch only) |
| **Deploy to Staging**  | Deploys to staging environment          |

## Running Locally with Docker

### Build and Run:

```bash
# Build the application
./mvnw package -DskipTests

# Run with Docker Compose
docker-compose up --build
```

### Access the Application:

- API: http://localhost:8080
- MySQL: localhost:3306 (user: root, password: root)

## Environment Variables

Configure these in Jenkins:

| Variable          | Description         | Example            |
| ----------------- | ------------------- | ------------------ |
| `DOCKER_IMAGE`    | Docker image name   | `pirul-fireshield` |
| `DOCKER_REGISTRY` | Docker registry URL | `docker.io`        |
| `APP_VERSION`     | Application version | `0.0.1-SNAPSHOT`   |

## Troubleshooting

### Common Issues:

1. **Maven wrapper not executable on Windows**

   ```cmd
   # Run in CMD:
   mvnw.cmd clean package
   ```

2. **Docker build fails**
   - Ensure Docker daemon is running
   - Check Docker file permissions

3. **Tests fail**
   - Check database connection settings
   - Verify MySQL is running

### View Build Logs:

Navigate to: **Jenkins > Your Job > Build Number > Console Output**

## Security Considerations

- Store sensitive credentials in Jenkins Credentials
- Use Jenkins secret variables for passwords
- Limit Docker privileged mode
- Use read-only database credentials for CI

## Next Steps

1. Add deployment to production environment
2. Set up webhook for automatic builds on Git push
3. Configure SonarQube for code quality analysis
4. Add automated security scanning (OWASP ZAP)
