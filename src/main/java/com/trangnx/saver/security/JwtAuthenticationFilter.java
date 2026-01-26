package com.trangnx.saver.security;

import com.trangnx.saver.repository.UserRepository;
import com.trangnx.saver.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository,
            @Lazy TokenBlacklistService tokenBlacklistService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Check if Authorization header exists and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT token
        jwt = authHeader.substring(7);

        try {
            // Check if token is blacklisted (logged out)
            if (tokenBlacklistService.isBlacklisted(jwt)) {
                System.out.println("DEBUG: Token is blacklisted (logged out)");
                filterChain.doFilter(request, response);
                return;
            }

            // Extract email from JWT
            userEmail = jwtService.extractEmail(jwt);

            // If email is valid and no authentication exists in context
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Load user from database
                var user = userRepository.findByEmail(userEmail).orElse(null);

                if (user == null) {
                    // User no longer exists - blacklist token
                    System.out.println("DEBUG: User not found in database, token should be blacklisted: " + userEmail);
                    tokenBlacklistService.blacklistToken(jwt);
                } else if (!user.getIsActive()) {
                    // User is inactive - blacklist token
                    System.out.println("DEBUG: User account is inactive: " + userEmail);
                    tokenBlacklistService.blacklistToken(jwt);
                } else {
                    // Validate token
                    if (jwtService.validateToken(jwt, userEmail)) {
                        // Create authentication token
                        UserDetails userDetails = CustomUserDetails.fromUser(user);
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Set authentication in security context
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        System.out.println("DEBUG: JWT authentication successful for user: " + userEmail);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("DEBUG: JWT authentication failed: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}