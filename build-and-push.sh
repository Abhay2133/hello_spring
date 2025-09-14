#!/bin/bash

# Docker Build and Push Script for hello-spring
# This script builds your Spring Boot app and pushes it to Docker Hub

set -e  # Exit on any error

# Configuration
DOCKER_USERNAME="abhaybisht01"
IMAGE_NAME="hello-spring"
TAG="latest"
FULL_IMAGE_NAME="${DOCKER_USERNAME}/${IMAGE_NAME}:${TAG}"

echo "🐳 Building and pushing Docker image: ${FULL_IMAGE_NAME}"
echo "=================================================="

# Check if Docker is running
if ! docker info &> /dev/null; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

# Build the Docker image
echo "📦 Building Docker image..."
docker build -t ${FULL_IMAGE_NAME} .

if [ $? -eq 0 ]; then
    echo "✅ Docker image built successfully!"
else
    echo "❌ Docker build failed!"
    exit 1
fi

# Tag with additional version if provided
if [ ! -z "$1" ]; then
    VERSION_TAG="${DOCKER_USERNAME}/${IMAGE_NAME}:$1"
    docker tag ${FULL_IMAGE_NAME} ${VERSION_TAG}
    echo "🏷️  Tagged image with version: ${VERSION_TAG}"
fi

# Login to Docker Hub (you'll be prompted for credentials)
echo "🔐 Please login to Docker Hub..."
echo "If you don't have an account, create one at https://hub.docker.com"
echo "After creating an account, you may need to create the repository:"
echo "   Repository name: ${DOCKER_USERNAME}/${IMAGE_NAME}"
echo ""
docker login --username ${DOCKER_USERNAME}

# Push the image
echo "🚀 Pushing image to Docker Hub..."
docker push ${FULL_IMAGE_NAME}

if [ ! -z "$1" ]; then
    docker push ${VERSION_TAG}
    echo "✅ Pushed version tag: ${VERSION_TAG}"
fi

echo "✅ Image pushed successfully!"
echo "📍 Image URL: ${FULL_IMAGE_NAME}"
echo ""
echo "🔗 You can now deploy this image on Render.com using:"
echo "   Image URL: ${FULL_IMAGE_NAME}"
echo ""
echo "💡 To run locally:"
echo "   docker run -p 8080:8080 ${FULL_IMAGE_NAME}"