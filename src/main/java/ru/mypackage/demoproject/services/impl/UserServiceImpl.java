package ru.mypackage.demoproject.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mypackage.demoproject.dto.UserDTO;
import ru.mypackage.demoproject.models.ApplicationUser;
import ru.mypackage.demoproject.models.Role;
import ru.mypackage.demoproject.repository.RoleRepository;
import ru.mypackage.demoproject.repository.UserRepository;
import ru.mypackage.demoproject.services.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAllUsers() {
        List<ApplicationUser> applicationUserList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();

        applicationUserList.forEach((u) -> {
            userDTOList.add(mapUserDTO(u));
        });

        return userDTOList;
    }

    @Transactional
    public void setOperator(String username) {
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User is not valid"));

        Role role = roleRepository.findByAuthority("OPERATOR").orElseThrow();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setAuthorities(roles);

        userRepository.save(user);
    }

    private UserDTO mapUserDTO(ApplicationUser user) {
        return new UserDTO(user.getId(), user.getUsername());
    }

}
