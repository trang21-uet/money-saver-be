package com.trangnx.saver.util;

import com.trangnx.saver.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for authentication-related operations
 */
public class AuthenticationHelper {

    /**
     * Get current authenticated user details
     * @return CustomUserDetails of authenticated user
     * @throws RuntimeException if user is not authenticated or invalid principal
     */
    public static CustomUserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            throw new RuntimeException("Invalid authentication principal");
        }

        return (CustomUserDetails) principal;
    }

    /**
     * Get current authenticated user ID from security context
     * @return User ID of authenticated user
     * @throws RuntimeException if user is not authenticated or invalid principal
     */
    public static Long getCurrentUserId() {
        return getCurrentUserDetails().getId();
    }

    /**
     * Get current authenticated user email from security context
     * @return Email of authenticated user
     * @throws RuntimeException if user is not authenticated or invalid principal
     */
    public static String getCurrentUserEmail() {
        return getCurrentUserDetails().getEmail();
    }
}