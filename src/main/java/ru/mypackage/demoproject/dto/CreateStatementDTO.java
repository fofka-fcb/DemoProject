package ru.mypackage.demoproject.dto;

import lombok.Getter;
import lombok.Setter;
import ru.mypackage.demoproject.models.StatementType;

@Getter
@Setter
public class CreateStatementDTO {

    String username;
    StatementType statementType;
    String statement;

}
