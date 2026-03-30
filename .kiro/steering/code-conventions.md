# Code Conventions

## Java Code Style

### General Formatting

- Indentation: 4 spaces (no tabs)
- Line length: 120 characters maximum
- Encoding: UTF-8
- Line endings: LF (Unix style)

### Naming Conventions

#### Classes

- PascalCase for class names
- Nouns or noun phrases
- Examples: `User`, `TransactionService`, `JwtAuthenticationFilter`

#### Interfaces

- PascalCase, same as classes
- No "I" prefix
- Examples: `UserRepository`, `AccountService`

#### Methods

- camelCase for method names
- Verbs or verb phrases
- Examples: `getUserById()`, `createTransaction()`, `isTokenValid()`

#### Variables

- camelCase for variables
- Descriptive names, avoid abbreviations
- Examples: `userId`, `accessToken`, `transactionList`

#### Constants

- UPPER_SNAKE_CASE for constants
- Examples: `MAX_LOGIN_ATTEMPTS`, `DEFAULT_PAGE_SIZE`

#### Packages

- lowercase, no underscores
- Examples: `com.trangnx.saver.service`, `com.trangnx.saver.dto`

### Class Structure Order

```java
public class ExampleClass {
    // 1. Static constants
    private static final String CONSTANT = "value";

    // 2. Instance fields
    private final DependencyService dependencyService;
    private String instanceField;

    // 3. Constructors
    public ExampleClass(DependencyService dependencyService) {
        this.dependencyService = dependencyService;
    }

    // 4. Public methods
    public void publicMethod() { }

    // 5. Protected methods
    protected void protectedMethod() { }

    // 6. Private methods
    private void privateMethod() { }

    // 7. Inner classes/enums
    public enum Status { ACTIVE, INACTIVE }
}
```

## Lombok Usage

### Required Annotations

Use Lombok to reduce boilerplate:

#### Entities

```java
@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    // fields
}
```

#### DTOs

```java
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    // fields
}
```

#### Services/Controllers

```java
@Service
@RequiredArgsConstructor  // Constructor injection for final fields
public class UserService {
    private final UserRepository userRepository;
    // methods
}
```

### Lombok Best Practices

- Use `@RequiredArgsConstructor` for dependency injection
- Use `@Builder` for objects with many fields
- Use `@Getter/@Setter` instead of `@Data` for entities (avoid equals/hashCode issues)
- Use `@Slf4j` for logging
- Avoid `@ToString` on entities with relationships (causes lazy loading issues)

## Dependency Injection

### Constructor Injection (Preferred)

```java
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    // Lombok generates constructor automatically
}
```

### Field Injection (Avoid)

```java
// DON'T DO THIS
@Autowired
private UserRepository userRepository;
```

## Exception Handling

### Custom Exceptions

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found with %s: %s", resource, field, value));
    }
}
```

### Throwing Exceptions

```java
// In service layer
public UserDTO getUserById(Long id) {
    return userRepository.findById(id)
        .map(this::convertToDTO)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
}
```

### Global Exception Handling

All exceptions are caught by `GlobalExceptionHandler` - don't catch exceptions in controllers.

## Transaction Management

### Service Layer

```java
@Service
@RequiredArgsConstructor
public class TransactionService {

    @Transactional(readOnly = true)
    public TransactionDTO getById(Long id) {
        // Read-only transaction
    }

    @Transactional
    public TransactionDTO create(TransactionDTO dto) {
        // Write transaction
    }
}
```

### Transaction Best Practices

- Use `@Transactional` on service methods, not controllers
- Use `readOnly = true` for read operations
- Keep transactions short
- Don't call external services within transactions

## Entity Design

### Base Entity Pattern

All entities extend `BaseEntity`:

```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### Entity Relationships

```java
// Many-to-One (owning side)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", nullable = false)
private User user;

// One-to-Many (inverse side)
@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Account> accounts = new ArrayList<>();
```

### Enum Mapping

```java
@Enumerated(EnumType.STRING)  // Always use STRING, not ORDINAL
@Column(name = "account_type")
private AccountType type;

public enum AccountType {
    CASH, BANK, CREDIT_CARD
}
```

## DTO Patterns

### Entity to DTO Conversion

```java
private UserDTO convertToDTO(User user) {
    return UserDTO.builder()
        .id(user.getId())
        .email(user.getEmail())
        .fullName(user.getFullName())
        .avatarUrl(user.getAvatarUrl())
        .provider(user.getProvider() != null ? user.getProvider().name() : null)
        .isActive(user.getIsActive())
        .build();
}
```

### DTO to Entity Conversion

```java
private User convertToEntity(UserDTO dto) {
    return User.builder()
        .email(dto.getEmail())
        .fullName(dto.getFullName())
        .avatarUrl(dto.getAvatarUrl())
        .build();
}
```

## Repository Patterns

### Query Methods

```java
public interface UserRepository extends JpaRepository<User, Long> {

    // Derived query methods
    Optional<User> findByEmail(String email);
    Optional<User> findByGoogleId(String googleId);
    List<User> findByIsActiveTrue();

    // Custom queries
    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
    List<User> findByEmailDomain(@Param("domain") String domain);

    // Native queries (use sparingly)
    @Query(value = "SELECT * FROM users WHERE created_at > :date", nativeQuery = true)
    List<User> findRecentUsers(@Param("date") LocalDateTime date);
}
```

## Logging

### Using SLF4J with Lombok

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    public UserDTO getUserById(Long id) {
        log.debug("Fetching user with id: {}", id);

        try {
            UserDTO user = // ... fetch user
            log.info("Successfully retrieved user: {}", id);
            return user;
        } catch (Exception e) {
            log.error("Error fetching user with id: {}", id, e);
            throw e;
        }
    }
}
```

### Log Levels

- `ERROR` - Errors that need immediate attention
- `WARN` - Potentially harmful situations
- `INFO` - Important business events
- `DEBUG` - Detailed diagnostic information
- `TRACE` - Very detailed diagnostic information

### Logging Best Practices

- Use parameterized logging: `log.info("User {}", userId)` not `log.info("User " + userId)`
- Don't log sensitive data (passwords, tokens)
- Log at appropriate levels
- Include context in error logs
- Use structured logging for production

## Comments and Documentation

### When to Comment

- Complex business logic
- Non-obvious algorithms
- Workarounds or hacks
- Public API methods

### When NOT to Comment

- Obvious code
- What the code does (code should be self-documenting)
- Commented-out code (delete it)

### JavaDoc

```java
/**
 * Retrieves user statistics including account balances and monthly totals.
 *
 * @param userId the ID of the user
 * @return user statistics DTO
 * @throws ResourceNotFoundException if user not found
 */
public UserStatsDTO getUserStats(Long userId) {
    // implementation
}
```

## Code Organization

### Package by Feature (Preferred)

Current structure follows package-by-layer, but consider feature-based for new modules:

```
com.trangnx.saver.
├── auth/
│   ├── AuthController.java
│   ├── AuthService.java
│   └── dto/
├── user/
│   ├── UserController.java
│   ├── UserService.java
│   ├── UserRepository.java
│   └── dto/
```

## Best Practices

1. Keep methods short (< 20 lines ideally)
2. Single Responsibility Principle - one class, one purpose
3. Don't Repeat Yourself (DRY)
4. Use meaningful variable names
5. Avoid magic numbers - use constants
6. Prefer composition over inheritance
7. Use Optional instead of null checks
8. Use streams for collection operations
9. Close resources properly (use try-with-resources)
10. Write self-documenting code

## Code Review Checklist

- [ ] Follows naming conventions
- [ ] Uses Lombok appropriately
- [ ] Has proper exception handling
- [ ] Uses constructor injection
- [ ] Includes appropriate logging
- [ ] Has unit tests
- [ ] No code duplication
- [ ] No commented-out code
- [ ] No hardcoded values
- [ ] Follows SOLID principles
