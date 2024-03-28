package ru.mypackage.demoproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Сущность для создания заявление/черновика")
public class CreateStatementDTO {

    @Schema(
            description = "Заявление",
            type = "string",
            example = "какое то заявление..."
    )
    String statement;

}
