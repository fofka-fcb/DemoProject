package ru.mypackage.demoproject.services;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mypackage.demoproject.dto.LoginResponseDTO;
import ru.mypackage.demoproject.models.ApplicationUser;
import ru.mypackage.demoproject.models.Role;
import ru.mypackage.demoproject.models.Token;
import ru.mypackage.demoproject.models.TokenType;
import ru.mypackage.demoproject.repository.RoleRepository;
import ru.mypackage.demoproject.repository.TokenRepository;
import ru.mypackage.demoproject.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class AuthenticationService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private TokenRepository tokenRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;

    public ApplicationUser registerUser(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        return userRepository.save(new ApplicationUser(username, encodedPassword, authorities));
    }

    public LoginResponseDTO loginUser(String username, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        ApplicationUser user = userRepository.findByUsername(username).get();
        String access_token = tokenService.generateAccessToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, access_token);
//        String refresh_token = tokenService.generateRefreshToken(user);

        return new LoginResponseDTO(user.getUsername(), (Set<Role>) user.getAuthorities(), access_token);
    }

    private void saveUserToken(ApplicationUser user, String jwtToken) {
        Token token = new Token();
        token.setUserId(user.getId());
        token.setTokenType(TokenType.BEARER);
        token.setToken(jwtToken);
        token.setExpired(false);

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(ApplicationUser user) {
        List<Token> tokenList = tokenRepository.findAllValidToken(user.getId(), false);

        if (tokenList.isEmpty())
            return;

        for (Token token : tokenList) {
            token.setExpired(true);
        }

        tokenRepository.saveAll(tokenList);
    }

}
