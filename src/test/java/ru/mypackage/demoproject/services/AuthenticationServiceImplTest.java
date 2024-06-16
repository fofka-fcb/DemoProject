package ru.mypackage.demoproject.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mypackage.demoproject.dto.responses.RegisterResponseDTO;
import ru.mypackage.demoproject.models.ApplicationUser;
import ru.mypackage.demoproject.models.Phone;
import ru.mypackage.demoproject.models.Role;
import ru.mypackage.demoproject.repository.PhoneRepository;
import ru.mypackage.demoproject.repository.RoleRepository;
import ru.mypackage.demoproject.repository.UserRepository;
import ru.mypackage.demoproject.services.impl.AuthenticationServiceImpl;
import ru.mypackage.demoproject.services.impl.init.InitAuthService;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private InitAuthService initAuthService;
    @InjectMocks
    private AuthenticationServiceImpl authService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private DaDataService daDataService;
    @Mock
    private PhoneRepository phoneRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    private Phone phone;
    private Role role;
    private ApplicationUser user;
    private RegisterResponseDTO regResponseDTO;

    @BeforeEach
    void init() {
        phone = new Phone("mob.", "89819700000", 123456, 123456);
        role = new Role(3, "USER");
        user = new ApplicationUser("user_1", "123456", Set.of(role));
        regResponseDTO =  new RegisterResponseDTO("user_1", "123456", Set.of(role));
    }

    @Test
    void shouldHaveCorrectRegisterUser() {
        when(initAuthService.getPasswordEncoder()).thenReturn(passwordEncoder);
        when(passwordEncoder.encode(any(String.class))).thenReturn("123456");

        when(initAuthService.getRoleRepository()).thenReturn(roleRepository);
        when(roleRepository.findByAuthority(any(String.class))).thenReturn(Optional.of(role));

        when(initAuthService.getDaDataService()).thenReturn(daDataService);
        when(daDataService.checkPhone(any(String.class))).thenReturn(phone);

        when(initAuthService.getPhoneRepository()).thenReturn(phoneRepository);
        when(phoneRepository.save(any(Phone.class))).thenReturn(phone);

        when(initAuthService.getUserRepository()).thenReturn(userRepository);
        when(userRepository.save(any(ApplicationUser.class))).thenReturn(user);

        when(initAuthService.getModelMapper()).thenReturn(modelMapper);
        when(modelMapper.map(any(ApplicationUser.class), any(Class.class))).thenReturn(regResponseDTO);

        RegisterResponseDTO regResponse =
                authService.registerUser("user_1", "123456", "89819700000");

        assertAll(
                () -> assertThat(regResponse.getUsername()).isEqualTo("user_1"),
                () ->assertThat(regResponse.getPassword()).isEqualTo("123456")
        );
    }

    @Test
    void shouldHaveCorrectLoginUser() {
    }
}
