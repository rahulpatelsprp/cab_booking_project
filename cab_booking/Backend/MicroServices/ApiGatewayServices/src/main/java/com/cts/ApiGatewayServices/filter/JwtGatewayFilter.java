package com.cts.ApiGatewayServices.filter;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.List;

@Slf4j
@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret.key}")
    private String secret;

    private static final String ID_HEADER_NAME = "X-User-ID";
    private static final String ROLES_HEADER_NAME = "X-User-Roles";
    private static final String ROLES_CLAIM = "role"; // Matches JwtUtil

    private final List<String> openApiEndpoints = List.of(
            "/api/users/login",
            "/api/users/register",
            "/api/drivers/register",
            "/v3/api-docs",
            "/swagger-ui/**",
            "/swagger-ui.html"
            );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (isFreeAccess(path)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info("Auth header is {}", authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return this.onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
    log.info("Token is {}", token);
        try {
            Key key = Keys.hmacShaKeyFor(secret.getBytes());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.info("Claims is {}", claims);
            String username = claims.getSubject();
            String role = claims.get(ROLES_CLAIM, String.class);
            log.info(username);
            log.info(role);
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header(ID_HEADER_NAME, username)
                    .header(ROLES_HEADER_NAME, role)
                    .build();
            log.info("Modified request is {}", modifiedRequest);

            log.info("Original headers: {}", request.getHeaders());
            log.info("Modified headers: {}", modifiedRequest.getHeaders());

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (SignatureException e) {
            return this.onError(exchange, "Invalid JWT signature", HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            return this.onError(exchange, "JWT token expired", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return this.onError(exchange, "JWT processing error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");

        String errorBody = "{\"Uncaught Exception\": \"" + message + "\"}";
        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorBody.getBytes())));
    }

    private boolean isFreeAccess(String path) {
        return openApiEndpoints.stream().anyMatch(path::contains);
    }

    @Override
    public int getOrder() {
        return -1;}
}