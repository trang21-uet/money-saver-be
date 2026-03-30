# Project Structure

## Backend Organization

```
src/main/java/com/trangnx/saver/
├── config/              # Spring configuration classes
│   ├── SecurityConfig.java       # Security & CORS configuration
│   ├── RedisConfig.java          # Redis cache configuration
│   ├── OpenApiConfig.java        # Swagger/OpenAPI setup
│   └── ResponseLoggingFilter.java
├── controller/          # REST API endpoints
│   ├── AuthController.java       # Authentication endpoints
│   ├── UserController.java       # User management
│   ├── AccountController.java    # Account operations
│   ├── TransactionController.java
│   ├── CategoryController.java
│   └── HealthController.java
├── service/             # Business logic layer
│   ├── UserService.java
│   ├── AccountService.java
│   ├── TransactionService.java
│   ├── CategoryService.java
│   ├── GoogleTokenVerificationService.java
│   └── TokenBlacklistService.java
├── repository/          # Data access layer (Spring Data JPA)
│   ├── UserRepository.java
│   ├── AccountRepository.java
│   ├── TransactionRepository.java
│   ├── CategoryRepository.java
│   └── BlacklistedTokenRepository.java
├── entity/              # JPA entities (database models)
│   ├── BaseEntity.java           # Common fields (id, timestamps)
│   ├── User.java
│   ├── Account.java
│   ├── Transaction.java
│   ├── Category.java
│   ├── Budget.java
│   ├── RefreshToken.java
│   └── BlacklistedToken.java
├── dto/                 # Data Transfer Objects (API contracts)
│   ├── ApiResponse.java          # Standard response wrapper
│   ├── ErrorResponse.java
│   ├── AuthResponse.java
│   ├── UserDTO.java
│   ├── AccountDTO.java
│   ├── TransactionDTO.java
│   └── CategoryDTO.java
├── security/            # Security components
│   ├── JwtService.java           # JWT generation & validation
│   ├── JwtAuthenticationFilter.java
│   ├── JwtAuthenticationEntryPoint.java
│   └── CustomUserDetails.java
├── exception/           # Exception handling
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── InvalidTokenException.java
├── util/                # Utility classes
│   ├── AuthenticationHelper.java
│   └── ResponseWrapper.java
└── MoneySaverApplication.java    # Main application class
```

## Architecture Patterns

### Layered Architecture

1. **Controller Layer**: REST endpoints, request validation, response formatting
2. **Service Layer**: Business logic, transaction management
3. **Repository Layer**: Data access via Spring Data JPA
4. **Entity Layer**: Domain models mapped to database tables

### Key Conventions

- **Entities**: Use Lombok annotations (`@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`)
- **Base Entity**: All entities extend `BaseEntity` for common fields (id, createdAt, updatedAt)
- **DTOs**: Separate DTOs for API contracts, never expose entities directly
- **API Responses**: Wrap all responses in `ApiResponse<T>` for consistency
- **Exception Handling**: Global exception handler catches and formats all errors
- **Security**: Stateless JWT authentication, no sessions
- **CORS**: Configured to allow all origins (update for production)

### Naming Conventions

- **Controllers**: `{Resource}Controller` (e.g., `UserController`)
- **Services**: `{Resource}Service` (e.g., `UserService`)
- **Repositories**: `{Entity}Repository` (e.g., `UserRepository`)
- **DTOs**: `{Resource}DTO` (e.g., `UserDTO`)
- **Entities**: Singular noun (e.g., `User`, `Transaction`)
- **API Endpoints**: `/api/{resource}` (e.g., `/api/users`, `/api/transactions`)

### Database Conventions

- **Table Names**: Automatically generated from entity names (lowercase with underscores)
- **Primary Keys**: `@Id` with `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- **Timestamps**: Managed by `@CreatedDate` and `@LastModifiedDate` in `BaseEntity`
- **Relationships**: Use `@ManyToOne`, `@OneToMany` with proper fetch strategies
- **Enums**: Use `@Enumerated(EnumType.STRING)` for readability

## Configuration Files

- `application.properties` - Main configuration
- `.env` - Environment-specific secrets (not committed)
- `build.gradle` - Dependencies and build configuration
- `docker-compose.yml` - Local development services (PostgreSQL, Redis)

## Spec Files

Specs are located in `.kiro/specs/{feature-name}/` with the following structure:

- `requirements.md` - Feature requirements and acceptance criteria
- `design.md` - Technical design and implementation details
- `tasks.md` - Implementation task breakdown
- `.config.kiro` - Spec metadata (workflow type, spec type)

Existing specs cover: auth, accounts, transactions, budgets, debts, groups, savings-goals, statistics, settings, OCR classification, and reports/sync/backup.
