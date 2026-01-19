package com.trangnx.saver.controller;

import com.trangnx.saver.dto.AuthResponse;
import com.trangnx.saver.entity.User;
import com.trangnx.saver.repository.UserRepository;
import com.trangnx.saver.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        String email = jwtService.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AuthResponse response = AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .token(token)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        try {
            System.out.println("DEBUG: Validating token: " + token.substring(0, 50) + "...");

            String email = jwtService.extractEmail(token);
            System.out.println("DEBUG: Extracted email: " + email);

            boolean isValid = jwtService.validateToken(token, email);
            System.out.println("DEBUG: Token is valid: " + isValid);

            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            System.out.println("DEBUG: Validation error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(false);
        }
    }
}