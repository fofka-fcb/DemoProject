package ru.mypackage.demoproject.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mypackage.demoproject.dto.*;
import ru.mypackage.demoproject.exceptions.UserNotRegisterException;
import ru.mypackage.demoproject.services.AuthenticationService;
import ru.mypackage.demoproject.utils.UserValidator;

import java.util.List;


@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserValidator userValidator;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserValidator userValidator) {
        this.authenticationService = authenticationService;
        this.userValidator = userValidator;
    }

    @PostMapping("/register")
    public RegisterResponseDTO registerUser(@RequestBody @Valid RegistrationDTO body, BindingResult bindingResult) {
        userValidator.validate(body, bindingResult);

        if (bindingResult.hasErrors()) throw new UserNotRegisterException(bindingResult);

        return authenticationService.registerUser(body.getUsername(), body.getPassword(), body.getPhone());
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(@RequestBody LoginDTO body) {
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }

}
