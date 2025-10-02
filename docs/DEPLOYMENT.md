# GitHub Actions Deployment Setup

> **Main Documentation**: [README.md](../README.md)  
> **Database Setup**: [DATABASE_MIGRATIONS.md](./DATABASE_MIGRATIONS.md)

This document explains how to configure the GitHub repository with the necessary secrets and environment variables for the deployment workflow.

## 📚 External Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Docker Hub Documentation](https://docs.docker.com/docker-hub/)
- [Render.com Documentation](https://render.com/docs)
- [Docker Buildx Documentation](https://docs.docker.com/buildx/working-with-buildx/)

## Overview

The GitHub Actions workflow (`deploy.yml`) automates:
1. **Testing** - Runs Maven tests on every push/PR
2. **Building** - Creates and pushes Docker images to Docker Hub
3. **Deployment** - Triggers manual deployment on Render.com

## Required Repository Configuration

### 1. Repository Secrets

Navigate to your GitHub repository → Settings → Secrets and variables → Actions → Repository secrets

Add the following secrets:

| Secret Name | Description | Example | Required |
|------------|-------------|---------|----------|
| `DOCKERHUB_USERNAME` | Your Docker Hub username | `abhay2133` | ✅ |
| `DOCKERHUB_TOKEN` | Docker Hub access token (not password!) | `dckr_pat_abcd...` | ✅ |

#### How to get Docker Hub Token:
1. Go to [Docker Hub](https://hub.docker.com/)
2. Login → Account Settings → Security → New Access Token
3. Create token with "Read, Write, Delete" permissions
4. Copy the token (you won't see it again!)

### 2. Repository Variables

Navigate to your GitHub repository → Settings → Secrets and variables → Actions → Repository variables

Add the following variables:

| Variable Name | Description | Example | Required |
|--------------|-------------|---------|----------|
| `RENDER_WEBHOOK_URL` | Render.com manual deploy webhook URL | `https://api.render.com/deploy/srv-abcd...` | ✅ |

#### How to get Render Webhook URL:
1. Go to your [Render Dashboard](https://dashboard.render.com/)
2. Select your service → Settings → Build & Deploy
3. Copy the "Deploy Hook" URL
4. Format: `https://api.render.com/deploy/srv-xxxxxxxxxxxxx`

## Workflow Triggers

The workflow runs on:
- **Push to main branch** - Full deployment pipeline
- **Pull requests** - Tests only
- **Manual trigger** - Via GitHub Actions UI

## Docker Image Configuration

The workflow creates multi-platform Docker images with these tags:
- `latest` (for main branch)
- `main-<commit-sha>` (for traceability)
- Branch name (for feature branches)

Example final image: `abhay2133/hello-spring:latest`

## Deployment Process

1. **Test Phase** ✅
   - Runs Maven tests
   - Caches dependencies for faster builds

2. **Build Phase** 🐳
   - Builds multi-platform Docker image (AMD64 + ARM64)
   - Pushes to Docker Hub with multiple tags
   - Uses BuildKit cache for efficiency

3. **Deploy Phase** 🚀
   - Triggers Render.com webhook
   - Waits for deployment to start
   - Reports status

## Security Notes

- Uses Docker Hub **access tokens** (not passwords) for better security
- Webhook URL is stored as repository variable (not secret) since it's not sensitive
- Multi-platform builds ensure compatibility across different architectures
- Dependency caching reduces build times and API calls

## Troubleshooting

### Common Issues:

1. **Docker Hub authentication failed**
   - Verify `DOCKERHUB_USERNAME` is correct
   - Ensure `DOCKERHUB_TOKEN` is an access token, not password
   - Check token permissions (Read, Write, Delete)

2. **Render webhook failed**
   - Verify `RENDER_WEBHOOK_URL` format is correct
   - Ensure the service exists in your Render account
   - Check Render service logs for deployment issues

3. **Tests failing**
   - Check test logs in GitHub Actions
   - Ensure all dependencies are properly defined in `pom.xml`
   - Verify Java 17 compatibility

### Manual Testing:

Test Docker Hub connection:
```bash
echo "$DOCKERHUB_TOKEN" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin
```

Test Render webhook:
```bash
curl -X POST "YOUR_RENDER_WEBHOOK_URL" -H "Content-Type: application/json"
```

## Environment Variables in Render

Your Render service should have these environment variables configured:
- `PORT=8080`
- `PING_ENABLED=true`
- `PING_URL=https://your-app.onrender.com`
- `PING_INTERVAL=5`
- `PING_LOG_LEVEL=INFO`

## Next Steps

1. Add the required secrets and variables to your GitHub repository
2. Push code to main branch to trigger the first deployment
3. Monitor the GitHub Actions tab for deployment progress
4. Check Render dashboard for service status

The workflow will automatically:
- Build and test your code
- Create Docker images
- Deploy to Render when everything passes