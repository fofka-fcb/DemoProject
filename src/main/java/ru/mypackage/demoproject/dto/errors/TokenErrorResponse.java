package ru.mypackage.demoproject.dto.errors;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Сущность для отправки сообщения о возникновании ошибки," +
        " связанной с токеном авторизации")
public class TokenErrorResponse {

    @Schema(
            description = "Сообщение о типе ошибки",
            type = "string",
            example = "token is expired"
    )
    private String message;

    @Schema(
            description = "Время возникновения ошибки",
            type = "long",
            example = "1711644439473"
    )
    private long timestamp;

}
