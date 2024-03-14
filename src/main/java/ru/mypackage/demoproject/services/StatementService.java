package ru.mypackage.demoproject.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mypackage.demoproject.dto.RefactorStatementDTO;
import ru.mypackage.demoproject.dto.StatementDTO;
import ru.mypackage.demoproject.exceptions.StatementNotFoundException;
import ru.mypackage.demoproject.exceptions.StatementSentException;
import ru.mypackage.demoproject.exceptions.TypeOfStatementNotValidException;
import ru.mypackage.demoproject.models.ApplicationUser;
import ru.mypackage.demoproject.models.Statement;
import ru.mypackage.demoproject.models.StatementType;
import ru.mypackage.demoproject.repository.StatementRepository;
import ru.mypackage.demoproject.repository.UserRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementRepository statementRepository;
    private final UserRepository userRepository;
    private final Integer draftsPerPage = 5;

    public StatementType checkStatus(Integer id) {
        return statementRepository.findById(id)
                .orElseThrow(() -> new StatementNotFoundException("Statement is not found!"))
                .getStatementType();
    }

    public StatementDTO findOne(Integer id, StatementType statementType) {

        Statement statement = statementRepository.findByIdAndStatementType(id, statementType)
                .orElseThrow(() -> new StatementNotFoundException("Statement is not found!"));

        return new StatementDTO(statement.getId(), statement.getStatementType(),
                statement.getCreateAt(), statement.getStatement());

    }

    public List<Statement> findAll(String username, StatementType statementType) {
        ApplicationUser user = findUser(username);

        return statementRepository.findAllByUserIdAndStatementType(user.getId(), statementType);
    }

    public List<Statement> findAllWithPaginationAndSort(String username,
                                                        StatementType statementType,
                                                        Integer page,
                                                        Boolean sortByData,
                                                        Boolean sortByDesc) {
        ApplicationUser user = findUser(username);

        if (sortByData && sortByDesc) {
            return statementRepository
                    .findAllByUserIdAndStatementType(user.getId(), statementType,
                            PageRequest.of(page, draftsPerPage, Sort.by("createAt").descending()));
        } else if (sortByData) {
            return statementRepository
                    .findAllByUserIdAndStatementType(user.getId(), statementType,
                            PageRequest.of(page, draftsPerPage, Sort.by("createAt")));
        } else {
            return statementRepository.findAllByUserIdAndStatementType(user.getId(), statementType,
                    PageRequest.of(page, draftsPerPage));
        }

    }

    public void create(String username, StatementType statementType, String statement) {
        statementRepository.save(mapStatement(username, statementType, statement));
    }

    public void sentStatementFromDrafts(Integer id) {
        Statement statement =statementRepository.findById(id)
                .orElseThrow(() -> new StatementNotFoundException("Statement is not found!"));

        if (statement.getStatementType().equals(StatementType.DRAFT)) {
            statement.setStatementType(StatementType.SENT);
            statementRepository.save(statement);
        } else {
            throw new StatementSentException("Statement has already been sent!");
        }
    }

    public void refactor(RefactorStatementDTO refStatementDTO) {
        Statement refStatement = statementRepository.findById(refStatementDTO.getId())
                .orElseThrow(() -> new StatementNotFoundException("Statement is not found!"));

        if (refStatement.getStatementType().equals(StatementType.DRAFT)) {
            refStatement.setStatement(refStatementDTO.getStatement());
            statementRepository.save(refStatement);
        } else {
            throw new TypeOfStatementNotValidException("Statement type is not valid!");
        }
    }

    private Statement mapStatement(String username, StatementType statementType, String statement) {
        ApplicationUser user = findUser(username);

        Statement createdStatement = new Statement();
        createdStatement.setUserId(user.getId());
        createdStatement.setStatement(statement);
        createdStatement.setStatementType(statementType);
        createdStatement.setCreateAt(new Date());

        return createdStatement;
    }

    private ApplicationUser findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User is not valid"));
    }
}
