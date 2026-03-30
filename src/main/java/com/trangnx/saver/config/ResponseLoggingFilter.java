package com.trangnx.saver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ResponseLoggingFilter implements Filter {

    private final ObjectMapper objectMapper;

    public ResponseLoggingFilter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.debug("Request: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());
        chain.doFilter(request, response);
    }
}
