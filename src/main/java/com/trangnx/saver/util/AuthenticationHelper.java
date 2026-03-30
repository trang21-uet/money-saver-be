package com.trangnx.saver.util;

import com.trangnx.saver.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationHelper {

    public static CustomUserDetails getCurrentUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return (CustomUserDetails) principal;
        }
        throw new IllegalStateException("No authenticated user found");
    }

    public static Long getCurrentUserId() {
        return getCurrentUserDetails().getId();
    }

    public static String getCurrentUserEmail() {
        return getCurrentUserDetails().getEmail();
    }
}
