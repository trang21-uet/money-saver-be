# Banana - Expense Management Application Setup Guide

A comprehensive expense management system built with Spring Boot backend and Flutter mobile app.

## Project Structure

### Backend (Spring Boot)

```
src/main/java/com/expensemanager/
├── config/              # Configuration classes (Security, CORS, Redis)
├── controller/          # REST API controllers
├── service/             # Business logic services
├── repository/          # Data access layer
├── model/
│   ├── entity/         # JPA entities
│   └── dto/            # Data Transfer Objects
├── security/           # JWT authentication & security
└── exception/          # Global exception handling
```

### Frontend (Flutter)

```
lib/
├── core/
│   ├── constants/      # App constants
│   ├── network/        # HTTP client (Dio)
│   ├── storage/        # Secure storage
│   └── utils/          # Utility classes
├── data/
│   ├── models/         # Data models
│   ├── repositories/   # Repository implementations
│   └── datasources/
│       ├── local/      # SQLite local storage
│       └── remote/     # API data sources
├── domain/
│   ├── entities/       # Business entities
│   ├── repositories/   # Repository interfaces
│   └── usecases/       # Business use cases
└── presentation/
    ├── screens/        # UI screens
    ├── widgets/        # Reusable widgets
    └── bloc/           # State management (BLoC)
```

## Prerequisites

- Java 17+
- PostgreSQL 14+
- Redis 6+
- Flutter 3.3+
- Dart 3.0+

## Backend Setup

### 1. Database Configuration

Create PostgreSQL databases:

```sql
CREATE DATABASE expense_manager_dev;
CREATE DATABASE expense_manager_test;
```

### 2. Environment Variables

Set the following environment variables:

```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=expense_manager_dev
DB_USERNAME=postgres
DB_PASSWORD=your_password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT
JWT_SECRET=YourSecretKeyHereMustBeAtLeast256BitsLongForHS512Algorithm
```

### 3. Run Backend

```bash
# Development mode
./gradlew bootRun --args='--spring.profiles.active=dev'

# Run tests
./gradlew test
```

The API will be available at `http://localhost:8080`

## Frontend Setup

### 1. Install Dependencies

```bash
flutter pub get
```

### 2. Configure API Base URL

Set the API base URL in environment:

```bash
flutter run --dart-define=API_BASE_URL=http://localhost:8080/api
```

### 3. Run Flutter App

```bash
# Development
flutter run

# Build for production
flutter build apk  # Android
flutter build ios  # iOS
```

## Features Implemented (Task 1)

- ✅ Spring Boot package structure
- ✅ Flutter clean architecture structure
- ✅ JWT Authentication configuration
- ✅ PostgreSQL database configuration
- ✅ Redis cache configuration
- ✅ CORS configuration
- ✅ Global exception handling
- ✅ Structured logging with Logback
- ✅ Secure storage setup for Flutter
- ✅ HTTP client (Dio) configuration

## API Documentation

Health check endpoint:

```
GET /api/health
```

Response:

```json
{
  "status": "UP",
  "timestamp": "2024-01-15T10:30:00",
  "service": "Expense Manager API"
}
```

## Configuration Files

### Backend

- `application.yml` - Main configuration
- `application-dev.yml` - Development profile
- `application-prod.yml` - Production profile
- `logback-spring.xml` - Logging configuration

### Frontend

- `pubspec.yaml` - Flutter dependencies
- `lib/core/constants/app_constants.dart` - App constants

## Security Configuration

- JWT token expiration: 7 days
- Password encryption: BCrypt
- CORS: Configured for all origins (update for production)
- Rate limiting: To be implemented in Task 4

## Next Steps

- Task 2: Create database schema and entities
- Task 3: Implement authentication and user management
- Task 4: Implement account management
- Task 5: Implement transaction management
