# Deployment Workflow

## Development Environment

### Local Setup

1. Install prerequisites:
   - Java 17+
   - PostgreSQL 14+
   - Redis 6+
   - Gradle 8.14.2 (included via wrapper)

2. Start infrastructure:

```bash
# Using Docker Compose
docker-compose up -d

# Or manually start PostgreSQL and Redis
```

3. Configure environment:

```bash
# Copy example env file
cp .env.example .env

# Edit .env with local credentials
```

4. Run application:

```bash
# Development mode with hot reload
./gradlew bootRun

# With specific profile
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Development Workflow

1. Create feature branch from `main`
2. Implement changes following code conventions
3. Write tests (unit + integration)
4. Run tests locally: `./gradlew test`
5. Check code coverage: `./gradlew jacocoTestReport`
6. Commit with descriptive message
7. Push and create pull request
8. Code review and approval
9. Merge to main

## Build Process

### Local Build

```bash
# Clean build
./gradlew clean build

# Build without tests (faster)
./gradlew clean build -x test

# Build production JAR
./gradlew bootJar
```

Output: `build/libs/banana-0.0.1-SNAPSHOT.jar`

### Build Verification

```bash
# Run all tests
./gradlew test

# Check test coverage
./gradlew jacocoTestReport

# Run specific test
./gradlew test --tests UserServiceTest

# Continuous testing
./gradlew test --continuous
```

## Database Management

### Schema Updates

Using JPA auto-create (current):

- `spring.jpa.hibernate.ddl-auto=update`
- Automatically creates/updates tables from entities
- Suitable for development only

### Migration Strategy (Future)

For production, use Flyway:

1. Disable JPA auto-create
2. Enable Flyway: `spring.flyway.enabled=true`
3. Create migration scripts in `src/main/resources/db/migration/`
4. Naming: `V{version}__{description}.sql`

## Testing Strategy

### Pre-Deployment Testing

1. Unit tests: `./gradlew test`
2. Integration tests: Included in test suite
3. Code coverage check: Minimum 80%
4. Manual smoke tests on staging

### Test Environments

- **Local**: Developer machine with Docker
- **Staging**: Mirrors production configuration
- **Production**: Live environment

## Deployment Environments

### Staging Deployment

1. Build application:

```bash
./gradlew clean build -x test
```

2. Deploy JAR to staging server

3. Set environment variables:

```bash
export SPRING_PROFILES_ACTIVE=staging
export DB_URL=jdbc:postgresql://staging-db:5432/expense_manager
export JWT_SECRET=<staging-secret>
```

4. Run application:

```bash
java -jar banana-0.0.1-SNAPSHOT.jar
```

5. Verify health:

```bash
curl http://staging-server:8080/api/health
```

### Production Deployment

1. Tag release:

```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

2. Build production artifact:

```bash
./gradlew clean build
```

3. Run production tests:

```bash
./gradlew test
```

4. Deploy to production server

5. Set production environment variables

6. Start application with production profile:

```bash
java -jar -Dspring.profiles.active=prod banana-0.0.1-SNAPSHOT.jar
```

7. Monitor logs and metrics

8. Verify deployment:

```bash
curl https://api.example.com/api/health
```

## Docker Deployment

### Build Docker Image

```dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY build/libs/banana-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:

```bash
docker build -t banana-api:latest .
docker run -p 8080:8080 --env-file .env banana-api:latest
```

### Docker Compose Production

```yaml
version: '3.8'
services:
  app:
    image: banana-api:latest
    ports:
      - '8080:8080'
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_URL=jdbc:postgresql://postgres:5432/expense_manager
    depends_on:
      - postgres
      - redis
    restart: unless-stopped
```

## Rollback Strategy

### Quick Rollback

1. Stop current application
2. Deploy previous version JAR
3. Restart application
4. Verify health endpoint

### Database Rollback

If using Flyway:

```bash
# Rollback last migration
./gradlew flywayUndo

# Rollback to specific version
./gradlew flywayUndo -Dflyway.target=V2
```

## Monitoring

### Health Checks

- Endpoint: `GET /api/health`
- Expected: `200 OK` with status "UP"
- Check frequency: Every 30 seconds

### Application Logs

```bash
# View logs
tail -f logs/application.log

# Search for errors
grep ERROR logs/application.log

# Monitor in real-time
tail -f logs/application.log | grep -i error
```

### Metrics to Monitor

- Response times (p50, p95, p99)
- Error rates (4xx, 5xx)
- Database connection pool usage
- Redis connection status
- JVM memory usage
- CPU utilization
- Active user sessions (JWT tokens issued)

### Logging Configuration

Production logging levels:

```properties
logging.level.root=WARN
logging.level.com.trangnx.saver=INFO
logging.level.org.springframework.security=WARN
logging.level.org.hibernate.SQL=WARN
```

## Backup Strategy

### Database Backups

```bash
# Daily backup
pg_dump -U postgres expense_manager > backup_$(date +%Y%m%d).sql

# Restore from backup
psql -U postgres expense_manager < backup_20240115.sql
```

### Redis Backups

Redis persistence configured:

- RDB snapshots every 15 minutes if changes
- AOF for durability

## Environment Configuration

### Development

- Debug logging enabled
- Hot reload active
- H2 console available (if configured)
- CORS allows all origins

### Staging

- Info logging
- Production-like data
- Limited CORS
- Performance monitoring

### Production

- Warn/Error logging only
- Real data
- Strict CORS
- Full monitoring and alerting
- HTTPS only
- Rate limiting enabled

## CI/CD Pipeline (Future)

### Recommended Pipeline

1. **Build Stage**
   - Checkout code
   - Run `./gradlew build`
   - Cache dependencies

2. **Test Stage**
   - Run unit tests
   - Run integration tests
   - Generate coverage report
   - Fail if coverage < 80%

3. **Security Scan**
   - Dependency vulnerability check
   - OWASP dependency check
   - Static code analysis

4. **Build Artifact**
   - Build production JAR
   - Build Docker image
   - Tag with version

5. **Deploy to Staging**
   - Deploy to staging environment
   - Run smoke tests
   - Manual approval gate

6. **Deploy to Production**
   - Blue-green deployment
   - Health check verification
   - Automatic rollback on failure

## Troubleshooting

### Common Issues

**Port already in use:**

```bash
# Find process
netstat -ano | findstr :8080
# Kill process or change port
```

**Database connection failed:**

- Check PostgreSQL is running
- Verify credentials in .env
- Check network connectivity

**Redis connection failed:**

- Verify Redis is running: `redis-cli ping`
- Check Redis host/port configuration

**Out of memory:**

- Increase JVM heap: `-Xmx2g`
- Check for memory leaks
- Review database connection pool size

## Deployment Checklist

- [ ] All tests passing
- [ ] Code coverage meets threshold
- [ ] Environment variables configured
- [ ] Database migrations ready
- [ ] Backup created
- [ ] Monitoring configured
- [ ] Health check endpoint working
- [ ] CORS configured correctly
- [ ] Security headers enabled
- [ ] HTTPS configured
- [ ] Rate limiting enabled
- [ ] Logs configured appropriately
- [ ] Rollback plan documented
- [ ] Team notified of deployment
