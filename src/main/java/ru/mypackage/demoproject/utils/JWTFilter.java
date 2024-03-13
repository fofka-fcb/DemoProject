package ru.mypackage.demoproject.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.mypackage.demoproject.exceptions.AppException;
import ru.mypackage.demoproject.repository.TokenRepository;
import ru.mypackage.demoproject.services.TokenService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().contains("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            String token = authorizationHeader.substring(7);
            Boolean validToken = tokenRepository.findByToken(token).map(t -> !t.getExpired()).orElse(false);

            if (validToken) {
                SecurityContextHolder.getContext().setAuthentication(tokenService.validateToken(token));
            } else {
                SecurityContextHolder.clearContext();
                throw new AppException("Token expired.", HttpStatus.UNAUTHORIZED);
            }

        }

        filterChain.doFilter(request, response);
    }
}
