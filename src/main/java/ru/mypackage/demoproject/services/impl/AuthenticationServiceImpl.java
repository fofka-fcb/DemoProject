package ru.mypackage.demoproject.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mypackage.demoproject.dto.responses.LoginResponseDTO;
import ru.mypackage.demoproject.dto.responses.RegisterResponseDTO;
import ru.mypackage.demoproject.models.*;
import ru.mypackage.demoproject.models.enums.TokenType;
import ru.mypackage.demoproject.services.AuthenticationService;
import ru.mypackage.demoproject.services.impl.init.InitAuthService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final InitAuthService initAuthService;

    @Autowired
    public AuthenticationServiceImpl(InitAuthService initAuthService) {
        this.initAuthService = initAuthService;
    }

    @Transactional
    public RegisterResponseDTO registerUser(String username, String password, String phoneNumber) {
        String encodedPassword = initAuthService.getPasswordEncoder().encode(password);
        Role userRole = initAuthService.getRoleRepository().findByAuthority("USER").get();

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        Phone phone = initAuthService.getDaDataService().checkPhone(phoneNumber);

        ApplicationUser applicationUser = new ApplicationUser(username, encodedPassword, authorities);

        if (phone != null) {
            phone.setUsername(applicationUser.getUsername());
            initAuthService.getPhoneRepository().save(phone);
        }

        initAuthService.getUserRepository().save(applicationUser);

        return convertToRegisterResponseDTO(applicationUser);
    }

    @Transactional
    public LoginResponseDTO loginUser(String username, String password) {
        Authentication auth = initAuthService.getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        ApplicationUser user = (ApplicationUser) auth.getPrincipal();
        String access_token = initAuthService.getTokenService().generateAccessToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, access_token);
//        String refresh_token = tokenService.generateRefreshToken(user);

        return new LoginResponseDTO(user.getUsername(), (Set<Role>) user.getAuthorities(), access_token);
    }

    protected void saveUserToken(ApplicationUser user, String jwtToken) {
        Token token = new Token();
        token.setUsername(user.getUsername());
        token.setTokenType(TokenType.BEARER);
        token.setToken(jwtToken);
        token.setExpired(false);

        initAuthService.getTokenRepository().save(token);
    }

    protected void revokeAllUserTokens(ApplicationUser user) {
        List<Token> tokenList = initAuthService.getTokenRepository().findAllValidToken(user.getUsername(), false);

        if (tokenList.isEmpty())
            return;

        for (Token token : tokenList) {
            token.setExpired(true);
        }

        initAuthService.getTokenRepository().saveAllAndFlush(tokenList);
    }

    private RegisterResponseDTO convertToRegisterResponseDTO(ApplicationUser user) {
        return initAuthService.getModelMapper().map(user, RegisterResponseDTO.class);
    }

    private ApplicationUser convertToApplicationUser(RegisterResponseDTO reg) {
        return initAuthService.getModelMapper().map(reg, ApplicationUser.class);
    }

}
