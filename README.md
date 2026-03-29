# Banana Expense Management - Backend API

Backend API cho ứng dụng quản lý chi tiêu Banana, được xây dựng bằng Spring Boot 3.5.3 và Java 17.

## Công nghệ sử dụng

- **Framework**: Spring Boot 3.5.3
- **Language**: Java 17
- **Database**: PostgreSQL
- **Cache**: Redis
- **Security**: Spring Security + JWT
- **Migration**: Flyway
- **Build Tool**: Gradle

## Yêu cầu hệ thống

- Java 17 hoặc cao hơn
- PostgreSQL 14 hoặc cao hơn
- Redis 6 hoặc cao hơn
- Gradle 8.14.2 (đã bao gồm wrapper)

## Cài đặt

### 1. Cài đặt PostgreSQL

```bash
# Tạo database
createdb banana_expense_db

# Hoặc sử dụng psql
psql -U postgres
CREATE DATABASE banana_expense_db;
```

### 2. Cài đặt Redis

```bash
# Ubuntu/Debian
sudo apt-get install redis-server
sudo systemctl start redis

# macOS
brew install redis
brew services start redis

# Windows
# Download từ https://redis.io/download
```

### 3. Cấu hình

Cập nhật `src/main/resources/application.properties` nếu cần:

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/banana_expense_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# JWT Secret (thay đổi trong production)
jwt.secret=YourSecretKeyForJWTTokenGenerationMustBeLongEnoughForHS512Algorithm
```

### 4. Chạy ứng dụng

```bash
# Build project
./gradlew build

# Run application
./gradlew bootRun

# Hoặc chạy JAR file
java -jar build/libs/banana-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### Health Check

```
GET /api/health
```

### Authentication (sẽ được implement trong Task 4)

```
POST /api/auth/register
POST /api/auth/login
POST /api/auth/refresh
POST /api/auth/forgot-password
POST /api/auth/reset-password
```

## Cấu trúc thư mục

```
src/main/java/com/trangnx/banana/
├── config/              # Configuration classes
│   ├── SecurityConfig.java
│   ├── CorsConfig.java
│   └── RedisConfig.java
├── controller/          # REST Controllers
├── service/             # Business logic
├── repository/          # Data access layer
├── model/
│   ├── entity/         # JPA Entities
│   └── dto/            # Data Transfer Objects
├── security/           # Security components
│   ├── JwtTokenProvider.java
│   └── JwtAuthenticationFilter.java
├── exception/          # Exception handling
│   ├── GlobalExceptionHandler.java
│   ├── ErrorResponse.java
│   ├── ResourceNotFoundException.java
│   └── DuplicateResourceException.java
└── BananaApplication.java

src/main/resources/
├── application.properties
└── db/migration/       # Flyway migration scripts
```

## Testing

```bash
# Run all tests
./gradlew test

# Run specific test
./gradlew test --tests ClassName

# Run with coverage
./gradlew test jacocoTestReport
```

## Database Migration

Flyway sẽ tự động chạy migration scripts khi khởi động ứng dụng.

Tạo migration script mới:

```
src/main/resources/db/migration/V{version}__{description}.sql
```

Ví dụ:

```
V1__create_users_table.sql
V2__create_accounts_table.sql
```

## Development

### Hot Reload

Sử dụng Spring Boot DevTools (đã được thêm vào dependencies):

```bash
./gradlew bootRun
```

### Debug

```bash
./gradlew bootRun --debug-jvm
```

Sau đó attach debugger vào port 5005.

## Production

### Build production JAR

```bash
./gradlew clean build -x test
```

### Run production

```bash
java -jar -Dspring.profiles.active=prod build/libs/banana-0.0.1-SNAPSHOT.jar
```

## Troubleshooting

### PostgreSQL connection error

- Kiểm tra PostgreSQL đang chạy: `sudo systemctl status postgresql`
- Kiểm tra credentials trong `application.properties`
- Kiểm tra database đã được tạo: `psql -l`

### Redis connection error

- Kiểm tra Redis đang chạy: `redis-cli ping` (should return PONG)
- Kiểm tra port: `netstat -an | grep 6379`

### Port 8080 already in use

- Thay đổi port trong `application.properties`: `server.port=8081`
- Hoặc kill process đang dùng port 8080

## Tài liệu tham khảo

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Flyway](https://flywaydb.org/documentation/)
- [JWT](https://jwt.io/)

## License

Private project - All rights reserved
