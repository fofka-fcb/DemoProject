package ru.mypackage.demoproject.services;

import org.springframework.beans.factory.annotation.Autowired;
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
public class StatementService {

    private final StatementRepository statementRepository;
    private final UserRepository userRepository;
    private final Integer perPage = 5;

    @Autowired
    public StatementService(StatementRepository statementRepository, UserRepository userRepository) {
        this.statementRepository = statementRepository;
        this.userRepository = userRepository;
    }

    public StatementType checkStatus(Integer id) {
        return findByIdFromRepo(id).getStatementType();
    }

    public Statement findOne(Integer id, StatementType statementType) {
        return findByIdAndStatementTypeFromRepo(id, statementType);
    }

    public Statement findOne(Integer id, String username) {
        ApplicationUser user = findUserFromRepo(username);
        return statementRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new StatementNotFoundException("Statement is not found!"));
    }

    public List<Statement> findAllSentStatements(Boolean sortByData, Boolean sortByDesc) {
        if (sortByDesc) return statementRepository
                .findAllByStatementType(StatementType.SENT, Sort.by("createAt").descending());

        else if (sortByData) return statementRepository
                .findAllByStatementType(StatementType.SENT, Sort.by("createAt"));

        else return statementRepository.findAll();
    }

    public List<Statement> findAllStatementsByType(StatementType statementType, Integer page,
                                                   Boolean sortByData, Boolean sortByDesc) {
        if (page == null && !sortByDesc) return statementRepository
                .findAllByStatementType(statementType, Sort.by("createAt"));

        else if (page == null) return statementRepository
                .findAllByStatementType(statementType, Sort.by("createAt").descending());

        else if (sortByDesc) return statementRepository
                .findAllByStatementType(statementType, PageRequest.of(page, perPage,
                        Sort.by("createAt").descending()));

        else return statementRepository
                    .findAllByStatementType(statementType, PageRequest.of(page, perPage,
                            Sort.by("createAt")));
    }

    public List<Statement> findAllByUsername(String username) {
        ApplicationUser user = findUserFromRepoByUsernameStartingWith(username);

        return statementRepository.findAllByUser(user);
    }

    public List<Statement> findAllByUsernameAndType(String username, StatementType statementType,
                                                    Boolean sortByData, Boolean sortByDesc) {
        ApplicationUser user = findUserFromRepoByUsernameStartingWith(username);

        if (sortByDesc)
            return statementRepository
                    .findAllByUserAndStatementType(user, statementType, Sort.by("createAt").descending());

        else if (sortByData)
            return statementRepository
                    .findAllByUserAndStatementType(user, statementType, Sort.by("createAt"));

        else return statementRepository.findAllByUserAndStatementType(user, statementType);
    }

    public List<Statement> findAllByUsernameAndPagination(String username, Integer page,
                                                          Boolean sortByData, Boolean sortByDesc) {
        ApplicationUser user = findUserFromRepoByUsernameStartingWith(username);

        if (sortByDesc) return statementRepository
                .findAllByUser(user, PageRequest.of(page, perPage, Sort.by("createAt").descending()));

        else if (sortByData) return statementRepository
                .findAllByUser(user, PageRequest.of(page, perPage, Sort.by("createAt")));

        else return statementRepository.findAllByUser(user, PageRequest.of(page, perPage));
    }

    public List<Statement> findAllByUsernameWithSorting(String username, Boolean sortByDate, Boolean sortByDesc) {
        ApplicationUser user = findUserFromRepoByUsernameStartingWith(username);

        if (sortByDesc) return statementRepository
                .findAllByUser(user, Sort.by("createAt").descending());

        else return statementRepository.findAllByUser(user, Sort.by("createAt"));
    }

    public List<Statement> findAllWithAllParameters(String username,
                                                    StatementType statementType,
                                                    Integer page,
                                                    Boolean sortByData,
                                                    Boolean sortByDesc) {

        ApplicationUser user = findUserFromRepoByUsernameStartingWith(username);

        if (sortByDesc) return statementRepository
                .findAllByUserAndStatementType(user, statementType,
                        PageRequest.of(page, perPage, Sort.by("createAt").descending()));

        else return statementRepository
                .findAllByUserAndStatementType(user, statementType,
                        PageRequest.of(page, perPage, Sort.by("createAt")));

    }

    //Создает и сохраняет заявку/черновик в репозиторий
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
        createdStatement.setUser(user);
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

    public ApplicationUser findUserFromRepo(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User is not valid"));
    }

}
