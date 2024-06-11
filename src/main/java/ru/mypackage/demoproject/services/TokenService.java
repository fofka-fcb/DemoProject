package ru.mypackage.demoproject.services;

import org.springframework.security.core.Authentication;
import ru.mypackage.demoproject.models.ApplicationUser;

public interface TokenService {

    String generateAccessToken(ApplicationUser user);

    String generateRefreshToken(ApplicationUser user);

    Authentication validateToken(String token);

    String parseToken(String token);

}
