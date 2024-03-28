package ru.mypackage.demoproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mypackage.demoproject.models.enums.StatementType;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Схема для заявления")
public class StatementDTO {

    @Schema(
            description = "'id'",
            type = "integer",
            example = "1"
    )
    private Integer id;

    @Schema(
            description = "Тип заявления",
            type = "string",
            example = "SENT"
    )
    private StatementType statementType;

    @Schema(
            description = "Дата создания",
            type = "date",
            example = "2024-03-18 01:15:34.266000"
    )
    private Date createdAt;

    @Schema(
            description = "Заявление",
            type = "string",
            example = "какое то заявление..."
    )
    private String statement;

}
