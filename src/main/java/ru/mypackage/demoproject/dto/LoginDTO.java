package ru.mypackage.demoproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность для авторизации")
public class LoginDTO {

    @Schema(
            description = "Имя пользователя",
            type = "string",
            example = "admin"
    )
    private String username;

    @Schema(
            description = "Пароль",
            type = "string",
            example = "password"
    )
    private String password;

}
