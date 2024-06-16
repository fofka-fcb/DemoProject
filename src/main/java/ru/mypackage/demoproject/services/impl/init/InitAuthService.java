package ru.mypackage.demoproject.services.impl.init;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.mypackage.demoproject.repository.PhoneRepository;
import ru.mypackage.demoproject.repository.RoleRepository;
import ru.mypackage.demoproject.repository.TokenRepository;
import ru.mypackage.demoproject.repository.UserRepository;
import ru.mypackage.demoproject.services.DaDataService;
import ru.mypackage.demoproject.services.TokenService;

@Component
public class InitAuthService {

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
    public InitAuthService(UserRepository userRepository, RoleRepository roleRepository,
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

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }

    public TokenRepository getTokenRepository() {
        return tokenRepository;
    }

    public PhoneRepository getPhoneRepository() {
        return phoneRepository;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public TokenService getTokenService() {
        return tokenService;
    }

    public DaDataService getDaDataService() {
        return daDataService;
    }
}
