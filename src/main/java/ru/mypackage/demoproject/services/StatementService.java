package ru.mypackage.demoproject.services;

import ru.mypackage.demoproject.dto.RefactorStatementDTO;
import ru.mypackage.demoproject.models.Statement;
import ru.mypackage.demoproject.models.enums.StatementType;

import java.util.List;

public interface StatementService {

    StatementType checkStatus(Integer id);

    Statement findOne(Integer id, StatementType statementType);

    Statement findOne(Integer id, String username);

    List<Statement> findAllStatementsByStatementType(String statementType, Integer page, Boolean sortByDesc);

    List<Statement> findAllStatementsByUser(String username, Integer page, Boolean sortByDesc);

    List<Statement> findAllStatementsByUserAndStatementType(String username, String statementType, Integer page, Boolean sortByDesc);

    void create(String username, StatementType statementType, String statement);

    void sentStatementFromDrafts(Integer id);

    void acceptSentStatement(Integer id);

    void rejectSentStatement(Integer id);

    void refactor(RefactorStatementDTO refStatementDTO);

}
