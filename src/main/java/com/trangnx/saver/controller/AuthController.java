package com.trangnx.saver.controller;

import com.trangnx.saver.dto.ApiResponse;
import com.trangnx.saver.dto.AuthResponse;
import com.trangnx.saver.dto.GoogleLoginRequest;
import com.trangnx.saver.entity.User;
import com.trangnx.saver.exception.InvalidTokenException;
import com.trangnx.saver.repository.UserRepository;
import com.trangnx.saver.security.JwtService;
import com.trangnx.saver.service.GoogleTokenVerificationService;
import com.trangnx.saver.service.TokenBlacklistService;
import com.trangnx.saver.util.AuthenticationHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final GoogleTokenVerificationService googleTokenVerificationService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponse>> getCurrentUser() {
        Long userId = AuthenticationHelper.getCurrentUserId();
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(ApiResponse.success(buildAuthResponse(user, null, null)));
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<AuthResponse>> loginWithGoogle(@RequestBody GoogleLoginRequest request) {
        try {
            var payload = googleTokenVerificationService.verifyIdToken(request.getIdToken());
            var userInfo = googleTokenVerificationService.extractUserInfo(payload);

            User user = userRepository.findByGoogleId(userInfo.getGoogleId())
                    .orElseGet(() -> userRepository.findByEmail(userInfo.getEmail())
                            .map(existing -> {
                                existing.setGoogleId(userInfo.getGoogleId());
                                existing.setAvatarUrl(userInfo.getAvatarUrl());
                                return userRepository.save(existing);
                            })
                            .orElseGet(() -> userRepository.save(User.builder()
                                    .email(userInfo.getEmail())
                                    .fullName(userInfo.getFullName())
                                    .avatarUrl(userInfo.getAvatarUrl())
                                    .googleId(userInfo.getGoogleId())
                                    .provider(User.AuthProvider.GOOGLE)
                                    .isActive(true)
                                    .build())));

            String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getId());
            String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getId());
            return ResponseEntity.ok(ApiResponse.success(buildAuthResponse(user, accessToken, refreshToken)));
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid Google token: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        String token = request.getRefreshToken();
        if (!jwtService.isRefreshToken(token)) throw new InvalidTokenException("Not a refresh token");
        if (tokenBlacklistService.isBlacklisted(token)) throw new InvalidTokenException("Token is blacklisted");

        String email = jwtService.extractEmail(token);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        tokenBlacklistService.blacklistToken(token);
        String newAccessToken = jwtService.generateAccessToken(email, user.getId());
        String newRefreshToken = jwtService.generateRefreshToken(email, user.getId());
        return ResponseEntity.ok(ApiResponse.success(buildAuthResponse(user, newAccessToken, newRefreshToken)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponse>> logout(HttpServletRequest request,
                                                               @RequestBody(required = false) LogoutRequest body) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            tokenBlacklistService.blacklistToken(authHeader.substring(7));
        }
        if (body != null && body.getRefreshToken() != null) {
            tokenBlacklistService.blacklistToken(body.getRefreshToken());
        }
        return ResponseEntity.ok(ApiResponse.success(new LogoutResponse("Logged out successfully")));
    }

    @GetMapping("/blacklist/count")
    public ResponseEntity<ApiResponse<BlacklistCountResponse>> getBlacklistCount() {
        return ResponseEntity.ok(ApiResponse.success(new BlacklistCountResponse(tokenBlacklistService.getBlacklistedTokensCount())));
    }

    private AuthResponse buildAuthResponse(User user, String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .userId(user.getId())
                .avatarUrl(user.getAvatarUrl())
                .expiresIn(jwtService.getAccessTokenExpiration())
                .build();
    }

    @Data
    public static class RefreshTokenRequest { private String refreshToken; }

    @Data
    public static class LogoutRequest { private String refreshToken; }

    @Data
    public static class LogoutResponse {
        private final String message;
    }

    @Data
    public static class BlacklistCountResponse {
        private final long count;
    }
}
