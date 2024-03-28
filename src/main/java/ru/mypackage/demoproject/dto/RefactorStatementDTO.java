package ru.mypackage.demoproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Сущность для редактирования черновика")
public class RefactorStatementDTO {

    @Schema(
            description = "'id'",
            type = "integer",
            example = "1"
    )
    private Integer id;

    @Schema(
            description = "Заявление",
            type = "string",
            example = "какое то заявление..."
    )
    private String statement;

}
