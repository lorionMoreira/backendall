# Docker Deployment Guide

## Quick Start

### Development Environment
```bash
# Start with dev environment
docker-compose --env-file .env.dev up -d --build

# View logs
docker-compose logs -f app

# Stop
docker-compose --env-file .env.dev down
```

### Production Environment
```bash
# Start with prod environment
docker-compose --env-file .env.prod up -d --build

# View logs
docker-compose logs -f app

# Stop (keep data)
docker-compose --env-file .env.prod down

# Stop (remove data)
docker-compose --env-file .env.prod down -v
```

## Available Commands

### Build
```bash
# Build only
docker-compose build

# Build with no cache
docker-compose build --no-cache
```

### Run
```bash
# Start in foreground
docker-compose --env-file .env.dev up

# Start in background
docker-compose --env-file .env.dev up -d

# Scale application
docker-compose --env-file .env.dev up -d --scale app=3
```

### Logs
```bash
# View all logs
docker-compose logs

# Follow logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f app
docker-compose logs -f db
```

### Management
```bash
# List running containers
docker-compose ps

# Execute command in container
docker-compose exec app sh

# Restart services
docker-compose restart

# Stop services
docker-compose stop

# Remove containers
docker-compose down

# Remove containers and volumes
docker-compose down -v
```

## Access Points

- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs
- **MySQL**: localhost:3306 (dev) or localhost:3307 (prod)

## Environment Files

- `.env.dev` - Development configuration
- `.env.prod` - Production configuration
- `.env.example` - Template for creating new environment files

## Security Notes

1. Change `JWT_SECRET` in production
2. Use strong passwords for `DB_ROOT_PASSWORD`
3. Never commit `.env.dev` or `.env.prod` to version control
4. Add `.env*` to `.gitignore` (except `.env.example`)

## Troubleshooting

### Database Connection Issues
```bash
# Check if database is ready
docker-compose exec db mysqladmin ping -h localhost -u root -p

# Check database logs
docker-compose logs db
```

### Application Won't Start
```bash
# Check application logs
docker-compose logs app

# Rebuild without cache
docker-compose build --no-cache app
```

### Reset Everything
```bash
# Stop and remove everything
docker-compose down -v

# Start fresh
docker-compose --env-file .env.dev up -d --build
```
