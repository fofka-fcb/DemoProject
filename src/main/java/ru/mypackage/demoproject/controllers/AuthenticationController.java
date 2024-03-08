package ru.mypackage.demoproject.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mypackage.demoproject.models.ApplicationUser;
import ru.mypackage.demoproject.models.dto.LoginResponseDTO;
import ru.mypackage.demoproject.models.dto.RegistrationDTO;
import ru.mypackage.demoproject.services.AuthenticationService;


@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@AllArgsConstructor
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ApplicationUser registerUser(@RequestBody RegistrationDTO body) {
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(@RequestBody RegistrationDTO body) {
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }

}
