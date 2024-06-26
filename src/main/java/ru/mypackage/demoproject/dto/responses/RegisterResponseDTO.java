package ru.mypackage.demoproject.dto.responses;

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
@Schema(description = "Сущность для отправки данных пользователя и ее ролей после регистрации")
public class RegisterResponseDTO {

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

    @Schema(
            description = "Роли",
            type = "application/json"
    )
    private Set<Role> authorities;

}
