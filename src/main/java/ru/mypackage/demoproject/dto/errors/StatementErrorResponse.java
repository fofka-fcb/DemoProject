package ru.mypackage.demoproject.dto.errors;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Сущность для отправки сообщения при возникновении ошибки," +
        " связанной с заявлением")
public class StatementErrorResponse {

    @Schema(
            description = "Сообщение о типе ошибки",
            type = "string",
            example = "statement with this 'id' wasn't found!"
    )
    private String message;

    @Schema(
            description = "Время возникновения ошибки",
            type = "long",
            example = "1711644439473"
    )
    private long timestamp;

}
