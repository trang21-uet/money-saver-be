package com.trangnx.saver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
public class ResponseLoggingFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper((HttpServletResponse) response);

        chain.doFilter(request, responseWrapper);

        // Log response for /api/auth/google endpoint
        if (httpRequest.getRequestURI().contains("/api/auth/google")) {
            byte[] content = responseWrapper.getContentAsByteArray();
            if (content.length > 0) {
                String responseBody = new String(content, responseWrapper.getCharacterEncoding());
                System.out.println("=== RESPONSE DEBUG ===");
                System.out.println("URI: " + httpRequest.getRequestURI());
                System.out.println("Status: " + responseWrapper.getStatus());
                System.out.println("Content-Type: " + responseWrapper.getContentType());
                System.out.println("Body: " + responseBody);
                System.out.println("======================");
            }
        }

        responseWrapper.copyBodyToResponse();
    }
}