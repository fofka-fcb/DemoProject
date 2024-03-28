package ru.mypackage.demoproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность для регистрации")
public class RegistrationDTO {

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    @Schema(
            description = "Имя пользователя",
            type = "string",
            example = "admin"
    )
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 4, max = 12, message = "Password should be between 4 and 12 characters")
    @Schema(
            description = "Пароль",
            type = "string",
            example = "password"
    )
    private String password;

    @NotEmpty(message = "Phone should not be empty")
    @Schema(
            description = "Номер телефона",
            type = "string",
            example = "89214285400"
    )
    private String phone;

}
