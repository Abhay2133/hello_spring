# Hello Spring Boot 🚀

A simple Spring Boot web application demonstrating containerized deployment with Docker and Render.com integration.

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Quick Start](#quick-start)
- [Development](#development)
- [Database Configuration](#database-configuration)
- [Docker](#docker)
- [Deployment](#deployment)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Documentation](#documentation)
- [Contributing](#contributing)
- [License](#license)
- [Links](#links)
- [Contact](#contact)

## ✨ Features

- **Simple REST API** with Spring Boot
- **Database Integration** with PostgreSQL (dev/prod) and H2 (testing)
- **JPA/Hibernate ORM** with automatic schema management
- **Environment-based Configuration** with multiple profiles
- **Containerized** with Docker multi-stage build
- **Auto-deployment** on Render.com
- **PR Previews** for testing changes
- **Health checks** and monitoring ready
- **Production-optimized** Docker image
- **Automatic URL Ping Service** to keep apps alive on free hosting

## 🛠 Technology Stack

- **Java 17** - Programming language
- **Spring Boot 3.5.5** - Application framework
- **Spring Data JPA** - Database abstraction layer with Hibernate
- **PostgreSQL** - Primary database for development and production
- **H2 Database** - In-memory database for testing
- **Maven 3.9.9** - Build tool
- **Docker** - Containerization
- **Render.com** - Cloud deployment platform

## 🚀 Quick Start

### Prerequisites

- Java 17 or later
- Maven 3.6+ (or use included wrapper)
- Docker (optional, for containerization)

### Run Locally

```bash
# Clone the repository
git clone https://github.com/Abhay2133/hello_spring.git
cd hello_spring

# Run with Maven wrapper
make run

# Or manually
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## 🔧 Development

### Available Make Commands

```bash
# Build the project
make build

# Run the application
make run

# Run tests
make test

# Clean build artifacts
make clean

# Package as JAR
make package

# Database migrations
make migrate              # Run pending migrations
make migrate-info         # Show migration status
make migrate-create name=add_column  # Create new migration
make migrate-validate     # Validate migrations

# Docker commands
make docker-build         # Build Docker image
make docker-run           # Run container
make docker-push          # Push to Docker Hub
make docker-stop          # Stop container

# Deployment
make deploy               # Deploy to Render.com

# Utility commands
make health               # Check app health
make status               # Show project status
make help                 # Show all commands
```

### Manual Commands

```bash
# Compile and run
./mvnw clean spring-boot:run

# Run tests
./mvnw test

# Package JAR
./mvnw clean package

# Skip tests during build
./mvnw clean package -DskipTests
```

## 🗄️ Database Configuration

This application uses **PostgreSQL** for development/production and **H2** for testing with **Hibernate** as the ORM, and **Flyway** for database migrations.

📚 **Detailed Documentation**: 
- [Database Migrations Guide](./docs/DATABASE_MIGRATIONS.md) - Complete Flyway migration documentation
- [Migration Quick Start](./docs/MIGRATION_SETUP.md) - Get started with migrations quickly

### Environment Setup

1. **Copy the environment template:**
   ```bash
   cp .env.example .env
   ```

2. **Configure your database connection in `.env`:**
   ```bash
   # Application Profile (dev, test, prod)
   SPRING_PROFILES_ACTIVE=dev

   # PostgreSQL Database Configuration
   DATABASE_URL=jdbc:postgresql://localhost:5432/hello_spring
   DATABASE_USERNAME=postgres
   DATABASE_PASSWORD=your_password

   # JPA/Hibernate Configuration
   HIBERNATE_DDL_AUTO=update
   JPA_SHOW_SQL=true
   ```

### Database Profiles

#### Development (`dev` profile)
- Uses **PostgreSQL** database
- `hibernate.ddl-auto=validate` - Validates schema (Flyway manages changes)
- SQL logging enabled for debugging
- Flyway auto-migration enabled

#### Testing (`test` profile) 
- Uses **H2** in-memory database
- `hibernate.ddl-auto=create-drop` - Creates fresh schema per test
- Automatic cleanup after tests

#### Production (`prod` profile)
- Uses **PostgreSQL** database  
- `hibernate.ddl-auto=validate` - Only validates existing schema
- SQL logging disabled for performance
- Flyway migrations run automatically

### PostgreSQL Setup

1. **Install PostgreSQL** (using Docker):
   ```bash
   docker run --name postgres-dev \
     -e POSTGRES_DB=hello_spring \
     -e POSTGRES_USER=postgres \
     -e POSTGRES_PASSWORD=password \
     -p 5432:5432 \
     -d postgres:15
   ```

2. **Or install locally:**
   - Ubuntu/Debian: `sudo apt install postgresql postgresql-contrib`
   - macOS: `brew install postgresql`
   - Windows: Download from postgresql.org

3. **Create database:**
   ```sql
   CREATE DATABASE hello_spring;
   ```

### API Endpoints for Database Demo

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/users?username=john&email=john@example.com` | Create a new user |
| GET | `/users` | Get all users |
| GET | `/users/search?username=john` | Find user by username |

## 🐳 Docker

### Local Docker Development

```bash
# Build image
make docker-build

# Run container
docker run -p 8080:8080 abhaybisht01/hello-spring:latest

# View logs
docker logs <container-id>
```

### Docker Image Details

- **Base Image**: Eclipse Temurin 17 JRE
- **Build Image**: Maven 3.9.9 with Eclipse Temurin 17
- **Multi-stage**: Optimized for production
- **Security**: Non-root user execution
- **Health Check**: Built-in health monitoring

## 🌐 Deployment

### Render.com Deployment

This project is configured for automatic deployment on Render.com using Docker and GitHub Actions.

📚 **Deployment Documentation**: [GitHub Actions Deployment Guide](./docs/DEPLOYMENT.md)

#### Quick Setup

1. **Auto-Deploy**: Pushes to `main` branch trigger deployment
2. **PR Previews**: Pull requests get preview URLs
3. **Health Checks**: Automatic monitoring on `/` endpoint
4. **Environment**: Containerized with Docker

#### Deployment Options

1. **Dockerfile Build** (Current):
   ```yaml
   env: docker
   dockerfilePath: ./Dockerfile
   ```

2. **Registry Image**:
   ```yaml
   env: docker
   image:
     url: abhaybisht01/hello-spring:latest
   ```

### Environment Variables

- `PORT`: Server port (default: 8080)
- `JAVA_TOOL_OPTIONS`: JVM optimization flags

## 📡 API Endpoints

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| GET | `/` | Welcome message | `"Greetings from Spring Boot!"` |
| GET | `/health` | Application health status | JSON with health info and ping status |
| GET | `/ping-status` | Ping service status | JSON with ping configuration and stats |

### Example Usage

```bash
# Test the main API
curl http://localhost:8080/

# Check application health
curl http://localhost:8080/health

# Check ping service status
curl http://localhost:8080/ping-status

# Response examples
curl http://localhost:8080/health
{
  "status": "UP",
  "message": "Hello Spring Boot is running",
  "timestamp": 1694678400000,
  "pingStatus": {
    "enabled": true,
    "url": "https://example.com",
    "intervalMinutes": 5,
    "totalPings": 42,
    "lastResult": "SUCCESS - Response time: 234ms",
    "lastPingTime": "2025-09-14 10:30:00"
  }
}
```

## 🔔 Ping Service (Keep-Alive)

The application includes an automatic ping service to keep your app alive on free hosting services like Render.com that sleep inactive apps.

### Configuration

Set these environment variables:

```bash
# Enable ping service
PING_ENABLED=true

# URL to ping (usually your own app URL)
PING_URL=https://your-app.onrender.com

# Ping interval in minutes (default: 5)
PING_INTERVAL=5

# Log level for ping service (default: INFO)
PING_LOG_LEVEL=INFO
```

### Features

- **Configurable interval** (default: 5 minutes)
- **Automatic logging** with timestamps and response times
- **Error handling** with retry logic
- **Status monitoring** via `/ping-status` endpoint
- **Health integration** in `/health` endpoint

## 📁 Project Structure

```
hello_spring/
├── docs/                                            # 📚 Documentation
│   ├── DATABASE_MIGRATIONS.md                      # Database migration guide
│   ├── MIGRATION_SETUP.md                          # Quick migration setup
│   └── DEPLOYMENT.md                               # Deployment configuration
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/hello_spring/
│   │   │       ├── HelloSpringApplication.java    # Main application
│   │   │       ├── AppConfig.java                 # Cache configuration
│   │   │       ├── controllers/
│   │   │       │   └── HelloController.java       # REST endpoints
│   │   │       ├── cron_jobs/
│   │   │       │   └── PingService.java           # Keep-alive service
│   │   │       ├── entities/
│   │   │       │   └── User.java                  # JPA entities
│   │   │       ├── repositories/
│   │   │       │   └── UserRepository.java        # Data access
│   │   │       └── services/
│   │   │           └── UserService.java           # Business logic
│   │   └── resources/
│   │       ├── application.properties              # Main configuration
│   │       ├── application-dev.properties          # Dev config
│   │       ├── application-prod.properties         # Prod config
│   │       ├── application-test.properties         # Test config
│   │       ├── db/migration/                       # Flyway migrations
│   │       │   └── V1__create_users_table.sql     # Initial schema
│   │       └── static/                             # Static web content
│   │           ├── index.html
│   │           ├── css/style.css
│   │           └── js/script.js
│   └── test/
│       └── java/
│           └── com/example/hello_spring/
│               └── HelloSpringApplicationTests.java # Tests
├── target/                                          # Build output
├── .mvn/wrapper/                                    # Maven wrapper
├── Dockerfile                                       # Docker configuration
├── .dockerignore                                    # Docker ignore rules
├── render.yaml                                      # Render.com config
├── Makefile                                         # Build automation
├── pom.xml                                          # Maven configuration
└── README.md                                        # This file
```

## 📚 Documentation

Comprehensive documentation is available in the [`docs/`](./docs/) directory:

| Document | Description | Link |
|----------|-------------|------|
| **Database Migrations** | Complete guide to Flyway migrations, creating migrations, examples | [DATABASE_MIGRATIONS.md](./docs/DATABASE_MIGRATIONS.md) |
| **Migration Quick Start** | Quick setup guide for getting started with migrations | [MIGRATION_SETUP.md](./docs/MIGRATION_SETUP.md) |
| **Deployment Guide** | GitHub Actions setup, Docker Hub, Render.com configuration | [DEPLOYMENT.md](./docs/DEPLOYMENT.md) |

### External Resources

- **Spring Boot**: [Official Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- **Spring Data JPA**: [Reference Guide](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- **Flyway**: [Documentation](https://flywaydb.org/documentation/)
- **PostgreSQL**: [Official Docs](https://www.postgresql.org/docs/)
- **Docker**: [Get Started Guide](https://docs.docker.com/get-started/)
- **Render.com**: [Documentation](https://render.com/docs)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🔗 Links

- **Live Demo**: [Deployed on Render.com]
- **Docker Hub**: [abhaybisht01/hello-spring](https://hub.docker.com/r/abhaybisht01/hello-spring)
- **GitHub**: [Abhay2133/hello_spring](https://github.com/Abhay2133/hello_spring)

## 📧 Contact

**Abhay** - [@Abhay2133](https://github.com/Abhay2133)

Project Link: [https://github.com/Abhay2133/hello_spring](https://github.com/Abhay2133/hello_spring)