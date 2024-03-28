package ru.mypackage.demoproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Сущность для отправки некоторых данных о пользователе")
public class UserDTO {

    @Schema(
            description = "'id' пользователя",
            type = "integer",
            example = "3"
    )
    private Integer id;

    @Schema(
            description = "Имя пользователя",
            type = "string",
            example = "user_1"
    )
    private String username;

}
