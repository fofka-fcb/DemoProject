package ru.mypackage.demoproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mypackage.demoproject.models.StatementType;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class StatementDTO {

    private Integer id;
    private StatementType statementType;
    private Date createdAt;
    private String statement;

}
