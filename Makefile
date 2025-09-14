# Makefile for Hello Spring Boot Project
# Author: Abhay (@Abhay2133)
# Description: Build automation for Spring Boot application with Docker support

# Variables
APP_NAME := hello-spring
DOCKER_USERNAME := abhaybisht01
DOCKER_IMAGE := $(DOCKER_USERNAME)/$(APP_NAME)
DOCKER_TAG := latest
FULL_IMAGE := $(DOCKER_IMAGE):$(DOCKER_TAG)
PORT := 8080
MAVEN_OPTS := -Dmaven.test.skip=false

# Colors for output
RED := \033[0;31m
GREEN := \033[0;32m
YELLOW := \033[0;33m
BLUE := \033[0;34m
PURPLE := \033[0;35m
CYAN := \033[0;36m
NC := \033[0m # No Color

# Default target
.DEFAULT_GOAL := help

# Phony targets
.PHONY: help build run test clean package docker-build docker-run docker-push docker-stop docker-clean deploy install dev-setup lint format check-updates status logs

## Help target
help: ## Show this help message
	@echo "$(CYAN)Hello Spring Boot - Available Commands$(NC)"
	@echo "$(YELLOW)=====================================$(NC)"
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "$(GREEN)%-15s$(NC) %s\n", $$1, $$2}' $(MAKEFILE_LIST)
	@echo ""
	@echo "$(BLUE)Docker Image:$(NC) $(FULL_IMAGE)"
	@echo "$(BLUE)Local URL:$(NC) http://localhost:$(PORT)"

## Development Commands
install: ## Install dependencies and setup project
	@echo "$(YELLOW)Installing dependencies...$(NC)"
	./mvnw dependency:resolve
	@echo "$(GREEN)Dependencies installed successfully!$(NC)"

build: ## Build the project
	@echo "$(YELLOW)Building Spring Boot application...$(NC)"
	./mvnw clean compile
	@echo "$(GREEN)Build completed successfully!$(NC)"

package: ## Package the application as JAR
	@echo "$(YELLOW)Packaging application...$(NC)"
	./mvnw clean package $(MAVEN_OPTS)
	@echo "$(GREEN)Package created: target/$(APP_NAME)-0.0.1-SNAPSHOT.jar$(NC)"

run: ## Run the application locally
	@echo "$(YELLOW)Starting Spring Boot application...$(NC)"
	@echo "$(BLUE)Application will be available at: http://localhost:$(PORT)$(NC)"
	./mvnw spring-boot:run

dev: ## Run in development mode with auto-reload
	@echo "$(YELLOW)Starting in development mode...$(NC)"
	./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev"

test: ## Run all tests
	@echo "$(YELLOW)Running tests...$(NC)"
	./mvnw test
	@echo "$(GREEN)All tests passed!$(NC)"

test-coverage: ## Run tests with coverage report
	@echo "$(YELLOW)Running tests with coverage...$(NC)"
	./mvnw clean test jacoco:report
	@echo "$(GREEN)Coverage report generated in target/site/jacoco/$(NC)"

clean: ## Clean build artifacts
	@echo "$(YELLOW)Cleaning build artifacts...$(NC)"
	./mvnw clean
	@echo "$(GREEN)Clean completed!$(NC)"

## Docker Commands
docker-build: ## Build Docker image
	@echo "$(YELLOW)Building Docker image: $(FULL_IMAGE)$(NC)"
	docker build -t $(FULL_IMAGE) .
	@echo "$(GREEN)Docker image built successfully!$(NC)"

docker-run: ## Run Docker container
	@echo "$(YELLOW)Running Docker container...$(NC)"
	@echo "$(BLUE)Application will be available at: http://localhost:$(PORT)$(NC)"
	docker run -d --name $(APP_NAME) -p $(PORT):$(PORT) $(FULL_IMAGE)
	@echo "$(GREEN)Container started successfully!$(NC)"

docker-run-interactive: ## Run Docker container in interactive mode
	@echo "$(YELLOW)Running Docker container in interactive mode...$(NC)"
	docker run -it --rm -p $(PORT):$(PORT) $(FULL_IMAGE)

docker-stop: ## Stop Docker container
	@echo "$(YELLOW)Stopping Docker container...$(NC)"
	docker stop $(APP_NAME) || true
	docker rm $(APP_NAME) || true
	@echo "$(GREEN)Container stopped!$(NC)"

docker-push: ## Push Docker image to registry
	@echo "$(YELLOW)Pushing Docker image to Docker Hub...$(NC)"
	./build-and-push.sh
	@echo "$(GREEN)Image pushed successfully!$(NC)"

docker-pull: ## Pull Docker image from registry
	@echo "$(YELLOW)Pulling Docker image from Docker Hub...$(NC)"
	docker pull $(FULL_IMAGE)
	@echo "$(GREEN)Image pulled successfully!$(NC)"

docker-clean: ## Clean Docker images and containers
	@echo "$(YELLOW)Cleaning Docker resources...$(NC)"
	docker stop $(APP_NAME) || true
	docker rm $(APP_NAME) || true
	docker rmi $(FULL_IMAGE) || true
	@echo "$(GREEN)Docker resources cleaned!$(NC)"

docker-logs: ## Show Docker container logs
	@echo "$(YELLOW)Showing container logs...$(NC)"
	docker logs -f $(APP_NAME)

## Deployment Commands
deploy: ## Deploy to Render.com (commit and push)
	@echo "$(YELLOW)Deploying to Render.com...$(NC)"
	git add .
	git status
	@read -p "Enter commit message: " msg; \
	git commit -m "$$msg" || true
	git push origin main
	@echo "$(GREEN)Deployment triggered! Check Render.com dashboard.$(NC)"

deploy-docker: ## Deploy using Docker image to Render.com
	@echo "$(YELLOW)Updating render.yaml for Docker registry deployment...$(NC)"
	@cp render-registry-examples.yaml render.yaml.backup
	@echo "$(GREEN)Backup created: render.yaml.backup$(NC)"
	@echo "$(YELLOW)Update render.yaml manually to use Docker registry, then run 'make deploy'$(NC)"

## Development Tools
format: ## Format code (if formatter is available)
	@echo "$(YELLOW)Formatting code...$(NC)"
	./mvnw spotless:apply || echo "$(RED)Spotless not configured. Skipping format.$(NC)"

lint: ## Lint the code
	@echo "$(YELLOW)Linting code...$(NC)"
	./mvnw checkstyle:check || echo "$(RED)Checkstyle not configured. Skipping lint.$(NC)"

check-updates: ## Check for dependency updates
	@echo "$(YELLOW)Checking for dependency updates...$(NC)"
	./mvnw versions:display-dependency-updates

## Utility Commands
status: ## Show project status
	@echo "$(CYAN)Project Status$(NC)"
	@echo "$(YELLOW)==============$(NC)"
	@echo "$(BLUE)Project Name:$(NC) $(APP_NAME)"
	@echo "$(BLUE)Docker Image:$(NC) $(FULL_IMAGE)"
	@echo "$(BLUE)Port:$(NC) $(PORT)"
	@echo ""
	@echo "$(BLUE)Git Status:$(NC)"
	@git status --short || echo "Not a git repository"
	@echo ""
	@echo "$(BLUE)Docker Images:$(NC)"
	@docker images | grep $(APP_NAME) || echo "No Docker images found"
	@echo ""
	@echo "$(BLUE)Running Containers:$(NC)"
	@docker ps | grep $(APP_NAME) || echo "No running containers"

logs: ## Show application logs (if running in Docker)
	@echo "$(YELLOW)Showing application logs...$(NC)"
	docker logs -f $(APP_NAME) 2>/dev/null || echo "$(RED)Container not running. Use 'make docker-run' first.$(NC)"

health: ## Check application health
	@echo "$(YELLOW)Checking application health...$(NC)"
	@curl -s http://localhost:$(PORT)/health && echo "$(GREEN)\nApplication is healthy!$(NC)" || echo "$(RED)Application is not responding$(NC)"

ping-status: ## Check ping service status
	@echo "$(YELLOW)Checking ping service status...$(NC)"
	@curl -s http://localhost:$(PORT)/ping-status | jq '.' 2>/dev/null || curl -s http://localhost:$(PORT)/ping-status || echo "$(RED)Application is not responding$(NC)"

test-ping: ## Test ping service with local configuration
	@echo "$(YELLOW)Testing ping service locally...$(NC)"
	@echo "$(BLUE)Starting application with ping enabled...$(NC)"
	PING_ENABLED=true PING_URL=http://localhost:$(PORT)/ PING_INTERVAL=1 ./mvnw spring-boot:run

## Quick Commands
quick-start: clean package docker-build docker-run ## Quick start: clean, build, package, docker build and run
	@echo "$(GREEN)Quick start completed! Application running at http://localhost:$(PORT)$(NC)"

full-deploy: clean test package docker-build docker-push deploy ## Full deployment pipeline
	@echo "$(GREEN)Full deployment pipeline completed!$(NC)"

dev-setup: install ## Setup development environment
	@echo "$(GREEN)Development environment setup completed!$(NC)"

## Info
info: ## Show project information
	@echo "$(CYAN)Hello Spring Boot Project Information$(NC)"
	@echo "$(YELLOW)====================================$(NC)"
	@echo "$(BLUE)Application:$(NC) Spring Boot Web Application"
	@echo "$(BLUE)Java Version:$(NC) 17"
	@echo "$(BLUE)Spring Boot Version:$(NC) 3.5.5"
	@echo "$(BLUE)Build Tool:$(NC) Maven 3.9.9"
	@echo "$(BLUE)Docker Registry:$(NC) Docker Hub"
	@echo "$(BLUE)Deployment Platform:$(NC) Render.com"
	@echo "$(BLUE)Repository:$(NC) https://github.com/Abhay2133/hello_spring"
	@echo "$(BLUE)Docker Hub:$(NC) https://hub.docker.com/r/$(DOCKER_USERNAME)/$(APP_NAME)"