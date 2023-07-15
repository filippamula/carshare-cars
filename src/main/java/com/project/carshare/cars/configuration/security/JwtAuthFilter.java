package com.project.carshare.cars.configuration.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        var webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/v1/api/user/auth/internal/verify")
                .defaultHeader(AUTHORIZATION, authHeader)
                .build()
                .method(HttpMethod.POST)
                .retrieve();

        var clientResponse = Objects.requireNonNull(webClient.toBodilessEntity().block());
        if (clientResponse.getStatusCode().is2xxSuccessful()){
            SecurityContextHolder.getContext().setAuthentication(jwtService.getAuthentication(authHeader.substring(7)));
        }
        filterChain.doFilter(request, response);
    }
}
