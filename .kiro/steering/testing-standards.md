# Testing Standards

## Testing Strategy

### Test Pyramid

1. **Unit Tests** (70%) - Test individual components in isolation
2. **Integration Tests** (20%) - Test component interactions
3. **End-to-End Tests** (10%) - Test complete user flows

## Unit Testing

### Service Layer Tests

Test business logic in isolation using mocks:

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserById_WhenUserExists_ReturnsUserDTO() {
        // Given
        User user = User.builder()
            .id(1L)
            .email("test@example.com")
            .fullName("Test User")
            .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        UserDTO result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_WhenUserNotFound_ThrowsException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
            () -> userService.getUserById(1L));
    }
}
```

### Repository Tests

Use `@DataJpaTest` for repository layer:

```java
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_WhenUserExists_ReturnsUser() {
        // Given
        User user = User.builder()
            .email("test@example.com")
            .fullName("Test User")
            .build();
        userRepository.save(user);

        // When
        Optional<User> result = userRepository.findByEmail("test@example.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test User", result.get().getFullName());
    }
}
```

## Integration Testing

### Controller Tests

Use `@WebMvcTest` for controller layer:

```java
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @Test
    void getUser_WhenAuthenticated_ReturnsUser() throws Exception {
        // Given
        UserDTO userDTO = UserDTO.builder()
            .id(1L)
            .email("test@example.com")
            .build();
        when(userService.getUserById(1L)).thenReturn(userDTO);

        // When & Then
        mockMvc.perform(get("/api/users/1")
                .header("Authorization", "Bearer valid-token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }
}
```

### Full Integration Tests

Use `@SpringBootTest` for complete application context:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AuthenticationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void loginWithGoogle_ValidToken_ReturnsAuthResponse() {
        // Given
        GoogleLoginRequest request = new GoogleLoginRequest("valid-google-token");

        // When
        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
            "/api/auth/google", request, ApiResponse.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }
}
```

## Test Naming Conventions

### Method Naming

Use pattern: `methodName_condition_expectedResult`

Examples:

- `getUserById_WhenUserExists_ReturnsUserDTO`
- `createTransaction_WithInvalidAmount_ThrowsValidationException`
- `deleteAccount_WhenNotFound_Returns404`

### Test Class Naming

- Unit tests: `{ClassName}Test`
- Integration tests: `{Feature}IntegrationTest`
- Repository tests: `{RepositoryName}Test`

## Test Data Management

### Test Fixtures

Create reusable test data builders:

```java
public class TestDataBuilder {

    public static User createTestUser() {
        return User.builder()
            .email("test@example.com")
            .fullName("Test User")
            .provider(User.AuthProvider.GOOGLE)
            .isActive(true)
            .build();
    }

    public static Account createTestAccount(User user) {
        return Account.builder()
            .name("Test Account")
            .type(Account.AccountType.CASH)
            .balance(BigDecimal.valueOf(1000))
            .user(user)
            .build();
    }
}
```

### Database State

- Use `@Transactional` on test classes for automatic rollback
- Use `@BeforeEach` to set up test data
- Use `@AfterEach` to clean up if needed
- Avoid test interdependencies

## Mocking Guidelines

### What to Mock

- External services (Google OAuth, email services)
- Repositories in service tests
- Services in controller tests
- Time-dependent operations

### What NOT to Mock

- DTOs and entities
- Simple utility classes
- Configuration classes
- The class under test

### Mockito Best Practices

```java
// Use @Mock and @InjectMocks
@Mock
private UserRepository userRepository;

@InjectMocks
private UserService userService;

// Verify interactions
verify(userRepository).save(any(User.class));
verify(userRepository, times(1)).findById(1L);
verify(userRepository, never()).delete(any());

// Argument captors for complex verification
ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
verify(userRepository).save(userCaptor.capture());
assertEquals("test@example.com", userCaptor.getValue().getEmail());
```

## Assertions

### JUnit 5 Assertions

```java
// Basic assertions
assertEquals(expected, actual);
assertNotEquals(unexpected, actual);
assertTrue(condition);
assertFalse(condition);
assertNull(value);
assertNotNull(value);

// Exception assertions
assertThrows(ResourceNotFoundException.class, () -> {
    userService.getUserById(999L);
});

// Multiple assertions
assertAll(
    () -> assertEquals("test@example.com", user.getEmail()),
    () -> assertEquals("Test User", user.getFullName()),
    () -> assertTrue(user.getIsActive())
);
```

## Test Coverage

### Coverage Goals

- Overall: 80% minimum
- Service layer: 90% minimum
- Controller layer: 80% minimum
- Repository layer: Custom queries only

### Running Coverage

```bash
# Generate coverage report
./gradlew test jacocoTestReport

# View report
open build/reports/jacoco/test/html/index.html
```

### Excluded from Coverage

- DTOs (data classes)
- Configuration classes
- Main application class
- Exception classes

## Test Organization

### Package Structure

Mirror production code structure:

```
src/test/java/com/trangnx/saver/
├── controller/
│   ├── AuthControllerTest.java
│   └── UserControllerTest.java
├── service/
│   ├── UserServiceTest.java
│   └── AccountServiceTest.java
├── repository/
│   └── UserRepositoryTest.java
├── security/
│   └── JwtServiceTest.java
└── integration/
    ├── AuthenticationIntegrationTest.java
    └── TransactionFlowIntegrationTest.java
```

## Best Practices

1. Write tests before or alongside production code
2. Keep tests simple and focused (one assertion per test when possible)
3. Use descriptive test names that explain the scenario
4. Follow AAA pattern: Arrange, Act, Assert
5. Don't test framework code (Spring, JPA)
6. Mock external dependencies
7. Use test data builders for complex objects
8. Keep tests fast (< 1 second per test)
9. Tests should be independent and repeatable
10. Clean up test data to avoid side effects

## Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests UserServiceTest

# Run specific test method
./gradlew test --tests UserServiceTest.getUserById_WhenUserExists_ReturnsUserDTO

# Run tests with logging
./gradlew test --info

# Run tests in continuous mode
./gradlew test --continuous
```
