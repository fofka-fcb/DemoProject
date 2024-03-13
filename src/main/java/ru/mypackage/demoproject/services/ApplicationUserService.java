package ru.mypackage.demoproject.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mypackage.demoproject.models.ApplicationUser;
import ru.mypackage.demoproject.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {

    private final UserRepository userRepository;

    public ApplicationUser findUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }

}
