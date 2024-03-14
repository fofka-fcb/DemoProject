package ru.mypackage.demoproject.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mypackage.demoproject.dto.RefactorStatementDTO;
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
    private final Integer perPage = 5;

    public StatementType checkStatus(Integer id) {
        return findByIdFromRepo(id).getStatementType();
    }

    public Statement findOne(Integer id, StatementType statementType) {
        return findByIdAndStatementTypeFromRepo(id, statementType);
    }

    public List<Statement> findAllStatementsByType(StatementType statementType) {
        return statementRepository.findAllByStatementType(statementType);
    }

    public List<Statement> findAllSentStatements() {
        return statementRepository.findAllByStatementType(StatementType.SENT);
    }

    public List<Statement> findAllByUsername(String username, StatementType statementType) {
        ApplicationUser user = findUserFromRepoByUsernameStartingWith(username);

        return statementRepository.findAllByUserIdAndStatementType(user.getId(), statementType);
    }

    public List<Statement> findAllWithPaginationAndSort(String username,
                                                        StatementType statementType,
                                                        Integer page,
                                                        Boolean sortByData,
                                                        Boolean sortByDesc) {

        ApplicationUser user = findUserFromRepoByUsernameStartingWith(username);

        if ((page == null)) {
            return statementRepository.findAllByUserIdAndStatementType(user.getId(), statementType);

        } else if (sortByData && sortByDesc) {
            return statementRepository
                    .findAllByUserIdAndStatementType(user.getId(), statementType,
                            PageRequest.of(page, perPage, Sort.by("createAt").descending()));

        } else if (sortByData) {
            return statementRepository
                    .findAllByUserIdAndStatementType(user.getId(), statementType,
                            PageRequest.of(page, perPage, Sort.by("createAt")));

        } else {
            return statementRepository.findAllByUserIdAndStatementType(user.getId(), statementType,
                    PageRequest.of(page, perPage));

        }

    }

    public void create(String username, StatementType statementType, String statement) {
        statementRepository.save(mapStatement(username, statementType, statement));
    }

    public void sentStatementFromDrafts(Integer id) {
        Statement statement = findByIdFromRepo(id);

        if (statement.getStatementType().equals(StatementType.DRAFT)) {
            statement.setStatementType(StatementType.SENT);
            statementRepository.save(statement);
        } else {
            throw new StatementSentException("Statement has already been sent!");
        }
    }

    public void acceptSentStatement(Integer id) {
        Statement statement = findByIdAndStatementTypeFromRepo(id, StatementType.SENT);

        statement.setStatementType(StatementType.ACCEPT);
        statementRepository.save(statement);
    }

    public void rejectSentStatement(Integer id) {
        Statement statement = findByIdAndStatementTypeFromRepo(id, StatementType.SENT);

        statement.setStatementType(StatementType.REJECT);
        statementRepository.save(statement);
    }

    public void refactor(RefactorStatementDTO refStatementDTO) {
        Statement refStatement = findByIdFromRepo(refStatementDTO.getId());

        if (refStatement.getStatementType().equals(StatementType.DRAFT)) {
            refStatement.setStatement(refStatementDTO.getStatement());
            statementRepository.save(refStatement);
        } else {
            throw new TypeOfStatementNotValidException("Statement type is not valid!");
        }
    }

    private Statement mapStatement(String username, StatementType statementType, String statement) {
        ApplicationUser user = findUserFromRepoByUsernameStartingWith(username);

        Statement createdStatement = new Statement();
        createdStatement.setUserId(user.getId());
        createdStatement.setStatement(statement);
        createdStatement.setStatementType(statementType);
        createdStatement.setCreateAt(new Date());

        return createdStatement;
    }

    private Statement findByIdFromRepo(Integer id) {
        return statementRepository.findById(id)
                .orElseThrow(() -> new StatementNotFoundException("Statement is not found!"));
    }

    private Statement findByIdAndStatementTypeFromRepo(Integer id, StatementType statementType) {
        return statementRepository.findByIdAndStatementType(id, statementType)
                .orElseThrow(() -> new StatementNotFoundException("Statement is not found!"));
    }

    private ApplicationUser findUserFromRepoByUsernameStartingWith(String username) {
        return userRepository.findByUsernameStartingWith(username)
                .orElseThrow(() -> new UsernameNotFoundException("User is not valid"));
    }

}
