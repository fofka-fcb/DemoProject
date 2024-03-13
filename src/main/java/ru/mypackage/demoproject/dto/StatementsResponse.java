package ru.mypackage.demoproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mypackage.demoproject.models.Statement;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StatementsResponse {

    private List<Statement> statements;

}
