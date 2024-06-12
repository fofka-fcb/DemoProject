package ru.mypackage.demoproject.services;

import ru.mypackage.demoproject.dto.responses.LoginResponseDTO;
import ru.mypackage.demoproject.dto.responses.RegisterResponseDTO;

public interface AuthenticationService {

    RegisterResponseDTO registerUser(String username, String password, String phoneNumber);

    LoginResponseDTO loginUser(String username, String password);

}
