package com.trangnx.saver.controller;

import com.trangnx.saver.dto.ApiResponse;
import com.trangnx.saver.dto.AuthResponse;
import com.trangnx.saver.dto.GoogleLoginRequest;
import com.trangnx.saver.entity.User;
import com.trangnx.saver.exception.ResourceNotFoundException;
import com.trangnx.saver.repository.UserRepository;
import com.trangnx.saver.security.CustomUserDetails;
import com.trangnx.saver.security.JwtService;
import com.trangnx.saver.service.GoogleTokenVerificationService;
import com.trangnx.saver.service.TokenBlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints with JWT access & refresh tokens")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final GoogleTokenVerificationService googleTokenVerificationService;

    @GetMapping("/me")
    @Operation(
            summary = "Get current user",
            description = "Get authenticated user information. Access token is automatically used from Authorization header.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<AuthResponse>> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body(ApiResponse.error(
                                "Not authenticated. Please provide valid Bearer token.",
                                "UNAUTHORIZED"
                        ));
            }

            // Check if principal is CustomUserDetails
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof CustomUserDetails userDetails)) {
                return ResponseEntity.status(401)
                        .body(ApiResponse.error(
                                "Invalid or expired token. Please login again.",
                                "INVALID_TOKEN"
                        ));
            }

            String email = userDetails.getEmail();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

            AuthResponse authResponse = AuthResponse.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .avatarUrl(user.getAvatarUrl())
                    .build();

            return ResponseEntity.ok(ApiResponse.success(authResponse));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error(e.getMessage(), "RESOURCE_NOT_FOUND"));
        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error(
                            "Invalid or expired token: " + e.getMessage(),
                            "UNAUTHORIZED"
                    ));
        }
    }

    @PostMapping("/google")
    @Operation(
            summary = "Login with Google (Client-side)",
            description = "Authenticate using Google ID token from google_sign_in package"
    )
    public ResponseEntity<ApiResponse<AuthResponse>> loginWithGoogle(
            @Valid @RequestBody GoogleLoginRequest request) {
        try {
            System.out.println("DEBUG: /api/auth/google - Received ID token: " +
                    request.getIdToken().substring(0, Math.min(50, request.getIdToken().length())) + "...");

            // Verify Google ID token
            var payload = googleTokenVerificationService.verifyIdToken(request.getIdToken());
            System.out.println("DEBUG: ID token verified successfully");

            var googleUserInfo = googleTokenVerificationService.extractUserInfo(payload);
            System.out.println("DEBUG: User info extracted - Email: " + googleUserInfo.getEmail() +
                    ", Name: " + googleUserInfo.getName());

            // Find or create user
            User user = userRepository.findByEmail(googleUserInfo.getEmail())
                    .orElseGet(() -> {
                        System.out.println("DEBUG: Creating new user for email: " + googleUserInfo.getEmail());
                        User newUser = User.builder()
                                .email(googleUserInfo.getEmail())
                                .fullName(googleUserInfo.getName())
                                .googleId(googleUserInfo.getGoogleId())
                                .avatarUrl(googleUserInfo.getPictureUrl())
                                .provider(User.AuthProvider.GOOGLE)
                                .isActive(true)
                                .build();
                        return userRepository.save(newUser);
                    });

            System.out.println("DEBUG: User found/created - ID: " + user.getId() + ", Email: " + user.getEmail());

            // Generate JWT tokens
            String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getId());
            String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getId());
            Long expiresIn = jwtService.getAccessTokenExpiration() / 1000; // seconds

            System.out.println("DEBUG: Tokens generated - Access token length: " + accessToken.length() +
                    ", Refresh token length: " + refreshToken.length() + ", Expires in: " + expiresIn + "s");

            AuthResponse authResponse = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userId(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .avatarUrl(user.getAvatarUrl())
                    .expiresIn(expiresIn)
                    .build();

            System.out.println("DEBUG: AuthResponse created - " +
                    "userId: " + authResponse.getUserId() +
                    ", email: " + authResponse.getEmail() +
                    ", accessToken present: " + (authResponse.getAccessToken() != null) +
                    ", refreshToken present: " + (authResponse.getRefreshToken() != null));

            ApiResponse<AuthResponse> response = ApiResponse.success("Login successful", authResponse);

            System.out.println("DEBUG: Final ApiResponse - success: " + response.isSuccess() +
                    ", message: " + response.getMessage() +
                    ", data present: " + (response.getData() != null));

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: Invalid token - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), "INVALID_TOKEN"));
        } catch (Exception e) {
            System.err.println("ERROR: Authentication failed - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Authentication failed: " + e.getMessage(), "AUTH_ERROR"));
        }
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh access token",
            description = "Get new access token using refresh token"
    )
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            String refreshToken = request.refreshToken();

            if (refreshToken == null || refreshToken.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Refresh token is required", "INVALID_REQUEST"));
            }

            if (tokenBlacklistService.isBlacklisted(refreshToken)) {
                return ResponseEntity.status(401)
                        .body(ApiResponse.error("Refresh token has been revoked", "TOKEN_REVOKED"));
            }

            if (!jwtService.isRefreshToken(refreshToken)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid token type. Expected refresh token", "INVALID_TOKEN_TYPE"));
            }

            String email = jwtService.extractEmail(refreshToken);
            Long userId = jwtService.extractUserId(refreshToken);

            if (!jwtService.validateToken(refreshToken, email)) {
                return ResponseEntity.status(401)
                        .body(ApiResponse.error("Invalid or expired refresh token", "TOKEN_EXPIRED"));
            }

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

            if (!user.getIsActive()) {
                return ResponseEntity.status(401)
                        .body(ApiResponse.error("User account is not active", "ACCOUNT_INACTIVE"));
            }

            String newAccessToken = jwtService.generateAccessToken(email, userId);
            Long expiresIn = jwtService.getAccessTokenExpiration() / 1000;

            AuthResponse authResponse = AuthResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .userId(userId)
                    .email(email)
                    .fullName(user.getFullName())
                    .avatarUrl(user.getAvatarUrl())
                    .expiresIn(expiresIn)
                    .build();

            System.out.println("DEBUG: Access token refreshed for user: " + email);

            return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", authResponse));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error(e.getMessage(), "RESOURCE_NOT_FOUND"));
        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Token refresh failed: " + e.getMessage(), "AUTH_ERROR"));
        }
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout",
            description = "Logout user and invalidate tokens. Access token is automatically used from Authorization header.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<LogoutResponse>> logout(
            HttpServletRequest request,
            @RequestBody(required = false) LogoutRequest logoutRequest) {
        try {
            // Get access token from Authorization header
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("No token provided", "INVALID_REQUEST"));
            }

            String accessToken = authHeader.substring(7);

            // Get current user info
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = null;
            if (authentication != null && authentication.isAuthenticated()) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                userEmail = userDetails.getEmail();
            }

            // Blacklist access token
            tokenBlacklistService.blacklistToken(accessToken);

            // Blacklist refresh token if provided
            if (logoutRequest != null && logoutRequest.refreshToken() != null) {
                tokenBlacklistService.blacklistToken(logoutRequest.refreshToken());
            }

            // Clear security context
            SecurityContextHolder.clearContext();

            System.out.println("DEBUG: User logged out: " + userEmail);

            LogoutResponse logoutResponse = new LogoutResponse(
                    true,
                    "Logged out successfully. Tokens invalidated.",
                    userEmail
            );

            return ResponseEntity.ok(ApiResponse.success(
                    "Logout successful",
                    logoutResponse
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(
                            "Logout failed: " + e.getMessage(),
                            "LOGOUT_ERROR"
                    ));
        }
    }

    @GetMapping("/blacklist/count")
    @Operation(
            summary = "Get blacklisted tokens count",
            description = "Get count of blacklisted tokens in Redis (for monitoring)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<BlacklistCountResponse>> getBlacklistCount() {
        long count = tokenBlacklistService.getBlacklistedTokensCount();
        BlacklistCountResponse response = new BlacklistCountResponse(count);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // Request/Response records
    private record RefreshTokenRequest(String refreshToken) {}

    private record LogoutRequest(String refreshToken) {}

    private record LogoutResponse(
            boolean success,
            String message,
            String email
    ) {
        LogoutResponse(boolean success, String message) {
            this(success, message, null);
        }
    }

    private record BlacklistCountResponse(long count) {}
}