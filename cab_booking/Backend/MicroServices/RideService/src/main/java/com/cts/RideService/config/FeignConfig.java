package com.cts.RideService.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@Slf4j
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                String userId = request.getHeader("X-User-ID");
                String role = request.getHeader("X-User-Roles");

                log.debug("Forwarding headers to Feign client:");
                log.debug("Authorization: {}", authHeader);
                log.debug("X-User-ID: {}", userId);
                log.debug("X-User-Roles: {}", role);

                if (authHeader != null) {
                    requestTemplate.header(HttpHeaders.AUTHORIZATION, authHeader);
                }
                if (userId != null) {
                    requestTemplate.header("X-User-ID", userId);
                }
                if (role != null) {
                    requestTemplate.header("X-User-Roles", role);
                }
            } else {
                log.warn("No ServletRequestAttributes found. Headers cannot be forwarded.");
            }
        };
    }
}
