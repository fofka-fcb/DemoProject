package ru.mypackage.demoproject.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import ru.mypackage.demoproject.models.Token;
import ru.mypackage.demoproject.repository.TokenRepository;

@Service
public class LogoutServiceImpl implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Autowired
    public LogoutServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return;
        }

        String jwt = authorizationHeader.substring(7);
        Token token = tokenRepository.findByToken(jwt).orElse(null);

        if (token != null ) {
            token.setExpired(true);
            tokenRepository.save(token);
            SecurityContextHolder.clearContext();
        }
    }
}
