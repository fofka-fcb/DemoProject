package ru.mypackage.demoproject.dto.errors;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Сущность для ответа в случае ошибки с именем пользователя или " +
        "валидации данных при регистрации")
public class UserErrorResponse {

    @Schema(
            description = "Сообщение о типе ошибки",
            type = "string",
            example = "user not found/username has been taken"
    )
    private String message;

    @Schema(
            description = "Время возникновения ошибки",
            type = "long",
            example = "1711644439473"
    )
    private long timestamp;
}
