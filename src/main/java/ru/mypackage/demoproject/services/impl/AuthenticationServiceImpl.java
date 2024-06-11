package ru.mypackage.demoproject.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mypackage.demoproject.dto.LoginResponseDTO;
import ru.mypackage.demoproject.dto.RegisterResponseDTO;
import ru.mypackage.demoproject.models.*;
import ru.mypackage.demoproject.models.enums.TokenType;
import ru.mypackage.demoproject.repository.PhoneRepository;
import ru.mypackage.demoproject.repository.RoleRepository;
import ru.mypackage.demoproject.repository.TokenRepository;
import ru.mypackage.demoproject.repository.UserRepository;
import ru.mypackage.demoproject.services.AuthenticationService;
import ru.mypackage.demoproject.services.DaDataService;
import ru.mypackage.demoproject.services.TokenService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PhoneRepository phoneRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final DaDataService daDataService;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                                     TokenRepository tokenRepository, PhoneRepository phoneRepository,
                                     PasswordEncoder passwordEncoder, ModelMapper modelMapper,
                                     AuthenticationManager authenticationManager, TokenService tokenService,
                                     DaDataService daDataService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.phoneRepository = phoneRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.daDataService = daDataService;
    }

    @Transactional
    public RegisterResponseDTO registerUser(String username, String password, String phoneNumber) {
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        Phone phone = daDataService.checkPhone(phoneNumber);

        ApplicationUser applicationUser = new ApplicationUser(username, encodedPassword, authorities);

        if (phone != null) {
            phone.setUsername(applicationUser.getUsername());
            phoneRepository.save(phone);
        }

        userRepository.save(applicationUser);

        return convertToRegisterResponseDTO(applicationUser);
    }

    @Transactional
    public LoginResponseDTO loginUser(String username, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        ApplicationUser user = (ApplicationUser) auth.getPrincipal();
        String access_token = tokenService.generateAccessToken(user);

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

        tokenRepository.save(token);
    }

    protected void revokeAllUserTokens(ApplicationUser user) {
        List<Token> tokenList = tokenRepository.findAllValidToken(user.getUsername(), false);

        if (tokenList.isEmpty())
            return;

        for (Token token : tokenList) {
            token.setExpired(true);
        }

        tokenRepository.saveAllAndFlush(tokenList);
    }

    private RegisterResponseDTO convertToRegisterResponseDTO(ApplicationUser user) {
        return modelMapper.map(user, RegisterResponseDTO.class);
    }

    private ApplicationUser convertToApplicationUser(RegisterResponseDTO reg) {
        return modelMapper.map(reg, ApplicationUser.class);
    }

}
