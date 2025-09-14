# Hello Spring Boot 🚀

A simple Spring Boot web application demonstrating containerized deployment with Docker and Render.com integration.

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Quick Start](#quick-start)
- [Development](#development)
- [Docker](#docker)
- [Deployment](#deployment)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Contributing](#contributing)

## ✨ Features

- **Simple REST API** with Spring Boot
- **Containerized** with Docker multi-stage build
- **Auto-deployment** on Render.com
- **PR Previews** for testing changes
- **Health checks** and monitoring ready
- **Production-optimized** Docker image

## 🛠 Technology Stack

- **Java 17** - Programming language
- **Spring Boot 3.5.5** - Application framework
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

# Build Docker image
make docker-build

# Push Docker image
make docker-push

# Deploy to Render.com
make deploy

# Show help
make help
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

This project is configured for automatic deployment on Render.com:

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

### Example Usage

```bash
# Test the API
curl http://localhost:8080/

# Response
"Greetings from Spring Boot!"
```

## 📁 Project Structure

```
hello_spring/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/hello_spring/
│   │   │       ├── HelloSpringApplication.java    # Main application
│   │   │       └── HelloController.java           # REST controller
│   │   └── resources/
│   │       └── application.properties              # Configuration
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