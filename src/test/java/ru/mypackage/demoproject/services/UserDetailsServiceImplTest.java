package ru.mypackage.demoproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import ru.mypackage.demoproject.models.ApplicationUser;
import ru.mypackage.demoproject.models.Role;
import ru.mypackage.demoproject.repository.UserRepository;
import ru.mypackage.demoproject.services.impl.UserDetailsServiceImpl;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldHaveCorrectLoadUserByUsername() {
        String username = "user_1";
        String password = "123456";
        Set<Role> authorities = Set.of(new Role(3, "USER"));
        ApplicationUser user = new ApplicationUser(username, password, authorities);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertAll(
                () -> assertThat(userDetails.getUsername()).isEqualTo(username),
                () -> assertThat(userDetails.getPassword()).isEqualTo(password),
                () -> assertThat(userDetails.getAuthorities()).isEqualTo(authorities)
        );
    }

}
