package ru.mypackage.demoproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mypackage.demoproject.dto.LoginDTO;
import ru.mypackage.demoproject.dto.LoginResponseDTO;
import ru.mypackage.demoproject.dto.RegisterResponseDTO;
import ru.mypackage.demoproject.dto.RegistrationDTO;
import ru.mypackage.demoproject.services.AuthenticationService;


@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public RegisterResponseDTO registerUser(@RequestBody RegistrationDTO body) {
        return authenticationService.registerUser(body.getUsername(), body.getPassword(), body.getPhone());
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(@RequestBody LoginDTO body) {
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }

}
