package ru.mypackage.demoproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mypackage.demoproject.models.Role;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность для отправки даных пользователя и jwt токена" +
        " в случае успешной авторизации")
public class LoginResponseDTO {

    @Schema(
            description = "Имя пользователя",
            type = "string",
            example = "admin"
    )
    private String username;

    @Schema(
            description = "Роли пользователя",
            type = "application/json"
    )
    private Set<Role> authorities;

    @Schema(
            description = "jwt",
            type = "string",
            example = "eyJhbGciOiJSUzI1NiJ9..."
    )
    private String jwt;

}
