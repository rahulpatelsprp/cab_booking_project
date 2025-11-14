package com.cts.DriverService.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class HeaderAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {



        String username = request.getHeader("X-User-ID");
        String role = request.getHeader("X-User-Roles");


        log.debug("Extracted headers - X-User-name: {}, ROLES_HEADER_NAME: {}", username, role);

        if (username != null && role != null) {
            try {
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);

                log.info("Authentication set for user: {} with role: {}", username, role);
            } catch (Exception e) {
                log.error("Error setting authentication context: {}", e.getMessage(), e);
            }
        } else {
            log.warn("Missing authentication headers: X-User-name or ROLES_HEADER_NAME");
        }

        filterChain.doFilter(request, response);
    }
}