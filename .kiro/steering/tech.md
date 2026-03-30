# Technology Stack

## Backend

- Java 17
- Spring Boot 3.2.0
- Spring Security with JWT authentication
- Spring Data JPA with Hibernate
- PostgreSQL 14+ (primary database)
- Redis 6+ (caching and token management)
- Gradle 8.14.2 (build tool)

## Key Libraries

- JJWT 0.12.3 (JWT token handling)
- Google API Client 2.2.0 (OAuth2 integration)
- SpringDoc OpenAPI 2.3.0 (API documentation)
- Lombok (boilerplate reduction)
- Jackson (JSON serialization)

## Development Tools

- Spring Boot DevTools (hot reload)
- JUnit Platform (testing)
- Logback (structured logging)

## Common Commands

### Build and Run

```bash
# Build project
./gradlew build

# Run application (development)
./gradlew bootRun

# Run with specific profile
./gradlew bootRun --args='--spring.profiles.active=dev'

# Build production JAR
./gradlew clean build -x test
```

### Testing

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests ClassName

# Run with coverage report
./gradlew test jacocoTestReport
```

### Database

```bash
# Create database
createdb expense_manager

# Run migrations (automatic on startup)
# JPA auto-creates tables from entities (spring.jpa.hibernate.ddl-auto=update)
```

### Redis

```bash
# Check Redis connection
redis-cli ping

# Monitor Redis commands
redis-cli monitor
```

## Configuration

Environment variables are used for sensitive configuration:

- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` - PostgreSQL connection
- `REDIS_HOST`, `REDIS_PORT`, `REDIS_PASSWORD` - Redis connection
- `JWT_SECRET` - JWT signing key (must be 256+ bits for HS512)
- `JWT_ACCESS_TOKEN_EXPIRATION` - Access token lifetime (default: 15 min)
- `JWT_REFRESH_TOKEN_EXPIRATION` - Refresh token lifetime (default: 7 days)
- `GOOGLE_CLIENT_ID` - Google OAuth2 client ID
- `SERVER_PORT` - Application port (default: 8080)

## API Documentation

Swagger UI available at: `http://localhost:8080/swagger-ui.html`

OpenAPI spec at: `http://localhost:8080/v3/api-docs`
