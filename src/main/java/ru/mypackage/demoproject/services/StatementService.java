package ru.mypackage.demoproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(readOnly = true)
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

    //Новый метод c пагинацией и сортировкой по типу заявки
    public List<Statement> findAllStatementsByStatementType(String statementType, Integer page, Boolean sortByDesc) {
        if (page != null && sortByDesc) return statementRepository
                .findAllByStatementType(StatementType.valueOf(statementType), PageRequest.of(page, perPage,
                        Sort.by("createAt").descending()));

        else if (page != null) return statementRepository
                .findAllByStatementType(StatementType.valueOf(statementType), PageRequest.of(page, perPage,
                        Sort.by("createAt")));

        else if (sortByDesc) return statementRepository
                .findAllByStatementType(StatementType.valueOf(statementType),
                        Sort.by("createAt").descending());

        else return statementRepository
                    .findAllByStatementType(StatementType.valueOf(statementType),
                            Sort.by("createAt"));
    }

    //Новый метод по поиску заявок с сортировкой и пагинацией по нахождению по юзеру
    public List<Statement> findAllStatementsByUser(String username, Integer page, Boolean sortByDesc) {
        ApplicationUser user = findUserFromRepoByUsernameStartingWith(username);

        if (page != null && sortByDesc) return statementRepository
                .findAllByUser(user, PageRequest.of(page, perPage,
                        Sort.by("createAt").descending()));

        else if (page != null) return statementRepository
                .findAllByUser(user, PageRequest.of(page, perPage,
                        Sort.by("createAt")));

        else if (sortByDesc) return statementRepository
                .findAllByUser(user, Sort.by("createAt").descending());

        else return statementRepository
                    .findAllByUser(user, Sort.by("createAt"));
    }

    //Новый мето по поиску с помощью пользователя и типа заявки
    public List<Statement> findAllStatementsByUserAndStatementType(String username,
                                                                   String statementType,
                                                                   Integer page,
                                                                   Boolean sortByDesc) {

        if (username == null) return findAllStatementsByStatementType(statementType, page, sortByDesc);
        else if (statementType == null) return findAllStatementsByUser(username, page, sortByDesc);
        else {
            ApplicationUser user = findUserFromRepoByUsernameStartingWith(username);

            if (page != null && sortByDesc) return statementRepository
                    .findAllByUserAndStatementType(user, StatementType.valueOf(statementType),
                            PageRequest.of(page, perPage,
                                    Sort.by("createAt").descending()));

            else if (page != null) return statementRepository
                    .findAllByUserAndStatementType(user, StatementType.valueOf(statementType),
                            PageRequest.of(page, perPage,
                                    Sort.by("createAt")));

            else if (sortByDesc) return statementRepository
                    .findAllByUserAndStatementType(user, StatementType.valueOf(statementType),
                            Sort.by("createAt").descending());

            else return statementRepository
                        .findAllByUserAndStatementType(user, StatementType.valueOf(statementType),
                                Sort.by("createAt"));
        }
    }

    //Создает и сохраняет заявку/черновик в репозиторий
    @Transactional
    public void create(String username, StatementType statementType, String statement) {
        statementRepository.save(mapStatement(username, statementType, statement));
    }

    @Transactional
    public void sentStatementFromDrafts(Integer id) {
        Statement statement = findByIdFromRepo(id);

        if (statement.getStatementType().equals(StatementType.DRAFT)) {
            statement.setStatementType(StatementType.SENT);
            statementRepository.save(statement);
        } else {
            throw new StatementSentException("Statement has already been sent!");
        }
    }

    @Transactional
    public void acceptSentStatement(Integer id) {
        Statement statement = findByIdAndStatementTypeFromRepo(id, StatementType.SENT);

        statement.setStatementType(StatementType.ACCEPT);
        statementRepository.save(statement);
    }

    @Transactional
    public void rejectSentStatement(Integer id) {
        Statement statement = findByIdAndStatementTypeFromRepo(id, StatementType.SENT);

        statement.setStatementType(StatementType.REJECT);
        statementRepository.save(statement);
    }

    @Transactional
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
