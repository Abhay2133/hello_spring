# Documentation Index

Welcome to the Hello Spring Boot documentation! This directory contains comprehensive guides for various aspects of the project.

## 📚 Available Documentation

### Core Documentation

| Document | Description | When to Use |
|----------|-------------|-------------|
| [**Main README**](../README.md) | Project overview, quick start, features | Starting point for new users |
| [**Database Migrations**](./DATABASE_MIGRATIONS.md) | Complete Flyway migration guide | Creating/managing database schema changes |
| [**Migration Quick Start**](./MIGRATION_SETUP.md) | Fast setup for migrations | Getting migrations running quickly |
| [**Deployment Guide**](./DEPLOYMENT.md) | GitHub Actions & Render.com setup | Setting up CI/CD and deployment |

## 🎯 Quick Navigation

### I want to...

- **Get started with the project** → [Main README](../README.md)
- **Set up database migrations** → [Migration Quick Start](./MIGRATION_SETUP.md)
- **Create a new migration** → [Database Migrations Guide](./DATABASE_MIGRATIONS.md#how-to-create-a-new-migration)
- **Deploy to Render.com** → [Deployment Guide](./DEPLOYMENT.md)
- **Configure GitHub Actions** → [Deployment Guide](./DEPLOYMENT.md#required-repository-configuration)
- **Understand Flyway** → [Database Migrations Guide](./DATABASE_MIGRATIONS.md)
- **Fix deployment errors** → [Deployment Guide](./DEPLOYMENT.md#troubleshooting)

## 📖 Documentation Overview

### 1. Main README ([../README.md](../README.md))

The central documentation for the project covering:
- ✨ Features and technology stack
- 🚀 Quick start guide
- 🔧 Development setup
- 🗄️ Database configuration
- 🐳 Docker usage
- 📡 API endpoints
- 📁 Project structure

**Start here if you're new to the project!**

### 2. Database Migrations Guide ([DATABASE_MIGRATIONS.md](./DATABASE_MIGRATIONS.md))

Comprehensive guide to database migrations with Flyway:
- 📁 Migration file structure and naming conventions
- 📝 Creating new migrations (tables, columns, indexes)
- 🔍 Migration examples and patterns
- ⚙️ Flyway configuration
- 🔧 Flyway Maven commands
- ⚠️ Important rules and best practices
- 🐛 Troubleshooting common issues

**Read this for detailed migration workflows.**

### 3. Migration Quick Start ([MIGRATION_SETUP.md](./MIGRATION_SETUP.md))

Fast-track guide for getting migrations running:
- ✅ What has been configured
- 🚀 Steps for Render.com deployment
- 📝 Creating your first migration
- 🔍 Important notes and tips
- 🐛 Fixing common errors

**Use this for quick reference and setup.**

### 4. Deployment Guide ([DEPLOYMENT.md](./DEPLOYMENT.md))

Complete CI/CD setup documentation:
- 📋 Required GitHub secrets and variables
- 🐳 Docker Hub configuration
- 🔗 Render.com webhook setup
- 🔄 Workflow triggers and process
- 🔒 Security best practices
- 🐛 Troubleshooting deployment issues
- ⚙️ Environment variable configuration

**Follow this to set up automated deployment.**

## 🔗 External Resources

### Spring Framework
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Boot Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)

### Database & Migrations
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Flyway Maven Plugin](https://flywaydb.org/documentation/usage/maven/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [H2 Database Documentation](http://www.h2database.com/html/main.html)

### DevOps & Deployment
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Docker Documentation](https://docs.docker.com/)
- [Docker Hub](https://docs.docker.com/docker-hub/)
- [Render.com Documentation](https://render.com/docs)

### Java & Build Tools
- [Java 17 Documentation](https://docs.oracle.com/en/java/javase/17/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [Maven POM Reference](https://maven.apache.org/pom.html)

## 🆘 Getting Help

1. **Check the relevant documentation** above based on your task
2. **Search for error messages** in the troubleshooting sections
3. **Review external resources** for in-depth understanding
4. **Check the main README** for project-specific configurations
5. **Open an issue** on GitHub if you're stuck

## 🤝 Contributing to Documentation

When updating documentation:
1. Keep it concise and focused
2. Use clear headings and formatting
3. Include code examples where helpful
4. Add cross-references to related docs
5. Update this index if adding new documents

## 📝 Document Changelog

- **2025-10-02**: Reorganized docs into `/docs` folder, added cross-references
- **2025-10-02**: Added Flyway migration documentation
- **2025-10-02**: Added deployment and CI/CD documentation

---

**Back to [Project README](../README.md)**
