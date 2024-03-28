package ru.mypackage.demoproject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mypackage.demoproject.dto.*;
import ru.mypackage.demoproject.dto.errors.UserErrorResponse;
import ru.mypackage.demoproject.exceptions.UserNotRegisterException;
import ru.mypackage.demoproject.services.AuthenticationService;
import ru.mypackage.demoproject.utils.UserValidator;


@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@Tag(name = "Auth controller", description = "Контроллер для регистрации новых пользователей")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserValidator userValidator;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserValidator userValidator) {
        this.authenticationService = authenticationService;
        this.userValidator = userValidator;
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация нового пользователя",
            description = "Данный метод регистрирует нового пользователя. Для регистрации необходимо " +
                    "указать имя, пароль и номер мобильного телефона. Для работы этого метода необходимо " +
                    "подключить сервис 'DaData', указав ключи в файле 'properties'",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Возвращает данные нового пользователя и его роль", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponseDTO.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Данные не прошли валидацию", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UserErrorResponse.class))
                    })
            })
    public RegisterResponseDTO registerUser(
            @RequestBody
            @Valid
            @Schema(description = "Сущность для регистрации", implementation = RegistrationDTO.class)
            RegistrationDTO body,
            BindingResult bindingResult
    ) {
        userValidator.validate(body, bindingResult);

        if (bindingResult.hasErrors()) throw new UserNotRegisterException(bindingResult);

        return authenticationService.registerUser(body.getUsername(), body.getPassword(), body.getPhone());
    }

    @PostMapping("/login")
    @Operation(summary = "Авторизация",
            description = "Данный метод позволяет пользователю авторизироваться по имени и паролю",
            responses = {
                    @ApiResponse(responseCode = "200", description = "При успешной авторизации возвращает имя, роль в системе и токен авторизации",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))
                            }),
                    @ApiResponse(responseCode = "404", description = "Если пользователь еще не зарегистрирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UserErrorResponse.class))
                    })
            })
    public LoginResponseDTO loginUser(
            @RequestBody
            @Schema(description = "Сущность для авторизации", implementation = LoginDTO.class)
            LoginDTO body
    ) {
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }

}
