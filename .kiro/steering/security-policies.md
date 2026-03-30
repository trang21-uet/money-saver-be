# Security Policies

## Authentication

### JWT Token Management

- Access tokens: 15 minutes expiration (configurable via `JWT_ACCESS_TOKEN_EXPIRATION`)
- Refresh tokens: 7 days expiration (configurable via `JWT_REFRESH_TOKEN_EXPIRATION`)
- Algorithm: HS512 (HMAC with SHA-512)
- Secret key: Minimum 256 bits, stored in environment variable `JWT_SECRET`

### Token Security

- Tokens are stateless and contain user ID and email
- Refresh tokens are single-use (blacklisted after refresh)
- Blacklisted tokens stored in Redis with TTL
- Token signature validation on every request
- No sensitive data in token payload

### Google OAuth2

- ID token verification using Google API Client
- Client ID validation against `GOOGLE_CLIENT_ID`
- User creation on first login
- Account linking by email if user exists

## Authorization

### Access Control

- Stateless authentication (no server-side sessions)
- User context extracted from JWT via `AuthenticationHelper.getCurrentUserId()`
- Resource ownership validation in service layer

### Protected Resources

All API endpoints except public routes require authentication:

- User can only access their own resources
- Validate user ownership before operations
- Return 403 Forbidden for unauthorized access

## Password Security

### Current Implementation

- No password-based authentication (Google OAuth only)
- Future: BCrypt with strength 12 for password hashing
- Future: Password complexity requirements (min 8 chars, mixed case, numbers)

## Data Protection

### Sensitive Data

Never log or expose:

- JWT tokens (access or refresh)
- Google OAuth tokens
- Database credentials
- API keys
- User passwords (if implemented)

### Database Security

- Use parameterized queries (JPA handles this)
- No raw SQL with string concatenation
- Connection pooling with HikariCP
- Credentials in environment variables, never in code

## API Security

### CORS Configuration

- Currently allows all origins (development only)
- Production: Restrict to specific frontend domains
- Credentials allowed for cookie-based auth (if needed)
- Allowed methods: GET, POST, PUT, DELETE, OPTIONS

### Rate Limiting

Not currently implemented. Future recommendations:

- 100 requests per minute per IP
- 1000 requests per hour per user
- Stricter limits on auth endpoints (5 login attempts per 15 minutes)

### Input Validation

- Validate all inputs using Jakarta Bean Validation
- Sanitize user inputs to prevent injection attacks
- Validate file uploads (size, type, content)
- Maximum request size: 5MB

### SQL Injection Prevention

- Use JPA/Hibernate for all database operations
- Parameterized queries for custom JPQL
- Avoid native SQL queries when possible
- Never concatenate user input into queries

## Session Management

### Stateless Architecture

- No server-side sessions
- JWT tokens for authentication state
- Redis for token blacklist only
- Session data in client-side tokens

### Token Lifecycle

1. Login: Generate access + refresh tokens
2. Request: Validate access token
3. Expiry: Use refresh token to get new tokens
4. Logout: Blacklist both tokens
5. Cleanup: Redis TTL removes expired blacklist entries

## Security Headers

Configure in production:

- `X-Content-Type-Options: nosniff`
- `X-Frame-Options: DENY`
- `X-XSS-Protection: 1; mode=block`
- `Strict-Transport-Security: max-age=31536000`
- `Content-Security-Policy: default-src 'self'`

## Error Handling

### Security in Error Messages

- Don't expose stack traces in production
- Generic error messages for authentication failures
- No information leakage about user existence
- Log detailed errors server-side only

## Dependency Security

### Vulnerability Management

- Regular dependency updates via Gradle
- Monitor security advisories for Spring Boot
- Use Dependabot or similar for automated alerts
- Review dependencies before adding

### Secure Dependencies

Current security-critical dependencies:

- Spring Security 6.x (latest stable)
- JJWT 0.12.3 (JWT handling)
- PostgreSQL driver (latest)
- Google API Client (OAuth2)

## Logging Security

### What to Log

- Authentication attempts (success/failure)
- Authorization failures
- Token refresh operations
- Account modifications
- Suspicious activities

### What NOT to Log

- Passwords or tokens
- Full request/response bodies with sensitive data
- Credit card or payment information
- Personal identifiable information (PII)

## Environment Security

### Configuration Management

- All secrets in environment variables
- Never commit `.env` files
- Use `.env.example` for documentation
- Different secrets per environment (dev/staging/prod)

### Required Environment Variables

```bash
# Database
DB_URL=jdbc:postgresql://localhost:5432/expense_manager
DB_USERNAME=postgres
DB_PASSWORD=<strong-password>

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=<redis-password>

# JWT
JWT_SECRET=<256-bit-secret>
JWT_ACCESS_TOKEN_EXPIRATION=900000
JWT_REFRESH_TOKEN_EXPIRATION=604800000

# OAuth
GOOGLE_CLIENT_ID=<google-client-id>
```

## Production Security Checklist

- [ ] Change all default passwords
- [ ] Use strong JWT secret (256+ bits)
- [ ] Enable HTTPS only
- [ ] Configure CORS for specific domains
- [ ] Implement rate limiting
- [ ] Enable security headers
- [ ] Disable debug logging
- [ ] Set up monitoring and alerting
- [ ] Regular security audits
- [ ] Keep dependencies updated
