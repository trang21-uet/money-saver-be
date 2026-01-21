package com.trangnx.saver.controller;

import com.trangnx.saver.dto.AuthResponse;
import com.trangnx.saver.entity.User;
import com.trangnx.saver.repository.UserRepository;
import com.trangnx.saver.security.CustomUserDetails;
import com.trangnx.saver.security.JwtService;
import com.trangnx.saver.service.TokenBlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/me")
    @Operation(
            summary = "Get current user",
            description = "Get authenticated user information. Access token is automatically used from Authorization header.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body("Not authenticated. Please provide valid Bearer token.");
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String email = userDetails.getEmail();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            AuthResponse response = AuthResponse.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .avatarUrl(user.getAvatarUrl())
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body("Invalid or expired token: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh access token",
            description = "Get new access token using refresh token"
    )
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            String refreshToken = request.refreshToken();

            // Validate refresh token
            if (refreshToken == null || refreshToken.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Refresh token is required"));
            }

            // Check if refresh token is blacklisted
            if (tokenBlacklistService.isBlacklisted(refreshToken)) {
                return ResponseEntity.status(401)
                        .body(new ErrorResponse("Refresh token has been revoked"));
            }

            // Verify it's a refresh token
            if (!jwtService.isRefreshToken(refreshToken)) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Invalid token type. Expected refresh token"));
            }

            // Extract user info
            String email = jwtService.extractEmail(refreshToken);
            Long userId = jwtService.extractUserId(refreshToken);

            // Validate token
            if (!jwtService.validateToken(refreshToken, email)) {
                return ResponseEntity.status(401)
                        .body(new ErrorResponse("Invalid or expired refresh token"));
            }

            // Verify user still exists and active
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.getIsActive()) {
                return ResponseEntity.status(401)
                        .body(new ErrorResponse("User account is not active"));
            }

            // Generate new access token
            String newAccessToken = jwtService.generateAccessToken(email, userId);
            Long expiresIn = jwtService.getAccessTokenExpiration() / 1000; // Convert to seconds

            AuthResponse response = AuthResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken) // Return same refresh token
                    .userId(userId)
                    .email(email)
                    .fullName(user.getFullName())
                    .avatarUrl(user.getAvatarUrl())
                    .expiresIn(expiresIn)
                    .build();

            System.out.println("DEBUG: Access token refreshed for user: " + email);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(new ErrorResponse("Token refresh failed: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout",
            description = "Logout user and invalidate tokens. Access token is automatically used from Authorization header.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<?> logout(HttpServletRequest request, @RequestBody(required = false) LogoutRequest logoutRequest) {
        try {
            // Get access token from Authorization header
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest()
                        .body(new LogoutResponse(false, "No token provided"));
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

            return ResponseEntity.ok(new LogoutResponse(
                    true,
                    "Logged out successfully. Tokens invalidated.",
                    userEmail
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new LogoutResponse(false, "Logout failed: " + e.getMessage()));
        }
    }

    @GetMapping("/blacklist/count")
    @Operation(
            summary = "Get blacklisted tokens count",
            description = "Get count of blacklisted tokens in Redis (for monitoring)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<?> getBlacklistCount() {
        long count = tokenBlacklistService.getBlacklistedTokensCount();
        return ResponseEntity.ok(new BlacklistCountResponse(count));
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

    private record ErrorResponse(String error) {}

    private record BlacklistCountResponse(long count) {}
}