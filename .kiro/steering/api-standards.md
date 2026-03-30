# API Standards

## REST API Design

### URL Structure

- Base path: `/api`
- Resource naming: Use plural nouns (`/api/users`, `/api/transactions`)
- Nested resources: `/api/accounts/{accountId}/transactions`
- Actions: Use HTTP verbs, avoid verbs in URLs
- Versioning: Not currently implemented (future: `/api/v1/...`)

### HTTP Methods

- `GET` - Retrieve resource(s)
- `POST` - Create new resource
- `PUT` - Update entire resource
- `PATCH` - Partial update (not currently used)
- `DELETE` - Remove resource

### Status Codes

Use appropriate HTTP status codes:

- `200 OK` - Successful GET, PUT, DELETE
- `201 Created` - Successful POST
- `204 No Content` - Successful DELETE with no response body
- `400 Bad Request` - Validation errors, malformed requests
- `401 Unauthorized` - Missing or invalid authentication
- `403 Forbidden` - Authenticated but insufficient permissions
- `404 Not Found` - Resource doesn't exist
- `500 Internal Server Error` - Unexpected server errors

## Response Format

### Standard Response Wrapper

All API responses must use `ApiResponse<T>`:

```java
{
  "timestamp": "2024-01-15T10:30:00",
  "success": true,
  "message": "Optional message",
  "data": { /* response payload */ },
  "error": null
}
```

### Success Response

```java
// With data
return ResponseEntity.ok(ApiResponse.success(data));

// With message and data
return ResponseEntity.ok(ApiResponse.success("Operation successful", data));

// Message only
return ResponseEntity.ok(ApiResponse.success("Operation completed"));
```

### Error Response

```java
{
  "timestamp": "2024-01-15T10:30:00",
  "success": false,
  "message": null,
  "data": null,
  "error": {
    "code": "ERROR_CODE",
    "message": "Human-readable error message",
    "detail": "Optional additional details"
  }
}
```

### Error Codes

Standard error codes used in the system:

- `TOKEN_EXPIRED` - JWT token has expired
- `INVALID_SIGNATURE` - JWT signature validation failed
- `MALFORMED_TOKEN` - JWT token format is invalid
- `INVALID_TOKEN` - Token is invalid or blacklisted
- `UNAUTHORIZED` - Authentication required
- `FORBIDDEN` - Insufficient permissions
- `BAD_CREDENTIALS` - Invalid login credentials
- `NOT_FOUND` - Resource not found
- `VALIDATION_ERROR` - Request validation failed
- `TYPE_MISMATCH` - Invalid parameter type
- `BAD_REQUEST` - General bad request
- `INTERNAL_ERROR` - Unexpected server error

## Request Validation

### Input Validation

Use Jakarta Bean Validation annotations:

```java
@NotNull(message = "Field cannot be null")
@NotBlank(message = "Field cannot be blank")
@Email(message = "Invalid email format")
@Size(min = 1, max = 100, message = "Length must be between 1 and 100")
@Min(value = 0, message = "Value must be positive")
@Pattern(regexp = "...", message = "Invalid format")
```

### Validation Error Response

Validation errors return `400 Bad Request` with combined error messages:

```java
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Email is required, Amount must be positive"
  }
}
```

## Authentication & Authorization

### JWT Authentication

- Access token: Short-lived (15 minutes default)
- Refresh token: Long-lived (7 days default)
- Token format: `Authorization: Bearer <token>`

### Public Endpoints

No authentication required:

- `/api/auth/**` - Authentication endpoints
- `/actuator/health` - Health check
- `/v3/api-docs/**` - API documentation
- `/swagger-ui/**` - Swagger UI

### Protected Endpoints

All other endpoints require valid JWT token in `Authorization` header.

## Pagination

Not currently implemented. Future standard:

```java
{
  "data": {
    "content": [...],
    "page": 0,
    "size": 20,
    "totalElements": 100,
    "totalPages": 5
  }
}
```

Query parameters: `?page=0&size=20&sort=createdAt,desc`

## Filtering & Sorting

Not currently standardized. Future recommendations:

- Filtering: `?status=active&type=expense`
- Sorting: `?sort=createdAt,desc`
- Date ranges: `?startDate=2024-01-01&endDate=2024-01-31`

## API Documentation

### OpenAPI/Swagger

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI spec: `http://localhost:8080/v3/api-docs`

### Controller Documentation

Use `@Operation`, `@ApiResponse`, `@Parameter` annotations:

```java
@Operation(summary = "Get user by ID", description = "Returns user details")
@ApiResponse(responseCode = "200", description = "User found")
@ApiResponse(responseCode = "404", description = "User not found")
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<UserDTO>> getUser(
    @Parameter(description = "User ID") @PathVariable Long id
) {
    // implementation
}
```

## Best Practices

1. Never expose entities directly - always use DTOs
2. Wrap all responses in `ApiResponse<T>`
3. Use `@Transactional` for write operations in service layer
4. Handle exceptions in `GlobalExceptionHandler`
5. Use meaningful error codes and messages
6. Log errors with appropriate levels (ERROR, WARN, INFO, DEBUG)
7. Validate all inputs at controller level
8. Use `@RequiredArgsConstructor` for dependency injection
9. Keep controllers thin - business logic belongs in services
10. Return appropriate HTTP status codes
