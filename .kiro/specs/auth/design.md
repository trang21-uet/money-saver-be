# Thiết Kế - Xác Thực và Bảo Mật

> Tham chiếu DB schema: `shared-architecture/design.md` (bảng `users`)

## API Endpoints

```
POST   /api/auth/register          - Đăng ký tài khoản
POST   /api/auth/login             - Đăng nhập
POST   /api/auth/refresh           - Refresh JWT token
POST   /api/auth/forgot-password   - Quên mật khẩu
POST   /api/auth/reset-password    - Đặt lại mật khẩu
```

## Backend Components

**JwtTokenProvider**: Generate và validate JWT token (7 ngày hết hạn)

**JwtAuthenticationFilter**: Interceptor kiểm tra JWT trên mỗi request

**AuthService**:

- `register(email, password)` → bcrypt hash password, lưu user
- `login(email, password)` → verify, trả JWT token
- `refreshToken(token)` → trả token mới
- `forgotPassword(email)` → gửi email reset link
- Rate limiting: 5 lần sai / 15 phút (dùng Redis counter)

## Flutter Components

**AuthBloc**: Quản lý state login/register/logout

**AuthRepository**: Gọi API, lưu JWT vào Flutter Secure Storage

**Biometric**: Dùng `local_auth` package cho Face ID / vân tay

## Security Config

```java
@Configuration
public class SecurityConfig {
    // Permit: /api/auth/**
    // Require JWT: tất cả endpoints còn lại
    // bcrypt strength: 12
}
```
