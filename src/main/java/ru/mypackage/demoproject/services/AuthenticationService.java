package ru.mypackage.demoproject.services;

import ru.mypackage.demoproject.dto.LoginResponseDTO;
import ru.mypackage.demoproject.dto.RegisterResponseDTO;

public interface AuthenticationService {

    RegisterResponseDTO registerUser(String username, String password, String phoneNumber);

    LoginResponseDTO loginUser(String username, String password);

}
