package ru.mypackage.demoproject.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mypackage.demoproject.dto.RefactorStatementDTO;
import ru.mypackage.demoproject.exceptions.StatementNotFoundException;
import ru.mypackage.demoproject.exceptions.StatementSentException;
import ru.mypackage.demoproject.exceptions.TypeOfStatementNotValidException;
import ru.mypackage.demoproject.models.Statement;
import ru.mypackage.demoproject.models.enums.StatementType;
import ru.mypackage.demoproject.repository.StatementRepository;
import ru.mypackage.demoproject.services.StatementService;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StatementServiceImpl implements StatementService {

    private final StatementRepository statementRepository;
    private final Integer perPage = 5;

    @Autowired
    public StatementServiceImpl(StatementRepository statementRepository) {
        this.statementRepository = statementRepository;
    }

    public StatementType checkStatus(Integer id) {
        return findByIdFromRepo(id).getStatementType();
    }

    public Statement findOne(Integer id, StatementType statementType) {
        return findByIdAndStatementTypeFromRepo(id, statementType);
    }

    public Statement findOne(Integer id, String username) {
        return statementRepository.findByIdAndUsername(id, username)
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

        if (page != null && sortByDesc) return statementRepository
                .findAllByUsername(username, PageRequest.of(page, perPage,
                        Sort.by("createAt").descending()));

        else if (page != null) return statementRepository
                .findAllByUsername(username, PageRequest.of(page, perPage,
                        Sort.by("createAt")));

        else if (sortByDesc) return statementRepository
                .findAllByUsername(username, Sort.by("createAt").descending());

        else return statementRepository
                    .findAllByUsername(username, Sort.by("createAt"));
    }

    //Новый мето по поиску с помощью пользователя и типа заявки
    public List<Statement> findAllStatementsByUserAndStatementType(String username,
                                                                   String statementType,
                                                                   Integer page,
                                                                   Boolean sortByDesc) {

        if (username == null) return findAllStatementsByStatementType(statementType, page, sortByDesc);
        else if (statementType == null) return findAllStatementsByUser(username, page, sortByDesc);
        else {

            if (page != null && sortByDesc) return statementRepository
                    .findAllByStatementTypeAndUsernameStartingWith(StatementType.valueOf(statementType),
                            username,
                            PageRequest.of(page, perPage,
                                    Sort.by("createAt").descending()));

            else if (page != null) return statementRepository
                    .findAllByStatementTypeAndUsernameStartingWith(StatementType.valueOf(statementType),
                            username,
                            PageRequest.of(page, perPage,
                                    Sort.by("createAt")));

            else if (sortByDesc) return statementRepository
                    .findAllByStatementTypeAndUsernameStartingWith(StatementType.valueOf(statementType),
                            username,
                            Sort.by("createAt").descending());

            else return statementRepository
                        .findAllByStatementTypeAndUsernameStartingWith(StatementType.valueOf(statementType),
                                username,
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
        Statement createdStatement = new Statement();
        createdStatement.setUsername(username);
        createdStatement.setStatement(statement);
        createdStatement.setStatementType(statementType);
        createdStatement.setCreateAt(new Date());

        return createdStatement;
    }

    protected Statement findByIdFromRepo(Integer id) {
        return statementRepository.findById(id)
                .orElseThrow(() -> new StatementNotFoundException("Statement is not found!"));
    }

    protected Statement findByIdAndStatementTypeFromRepo(Integer id, StatementType statementType) {
        return statementRepository.findByIdAndStatementType(id, statementType)
                .orElseThrow(() -> new StatementNotFoundException("Statement is not found!"));
    }

}
