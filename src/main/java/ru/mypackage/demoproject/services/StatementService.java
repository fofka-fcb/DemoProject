package ru.mypackage.demoproject.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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

    public void create(String username, StatementType statementType, String statement) {
        statementRepository.save(createStatement(username, statementType, statement));
    }

    public List<Statement> findAll(String username, StatementType statementType) {
        ApplicationUser user = findUser(username);

        return statementRepository.findAllByUserIdAndStatementType(user.getId(), statementType);
    }

    public List<Statement> findWithPaginationAndSort(String username,
                                                     StatementType statementType,
                                                     Integer page,
                                                     Integer draftsPerPage,
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

    private Statement createStatement(String username, StatementType statementType, String statement) {
        ApplicationUser user = findUser(username);

        Statement createdStatement = new Statement();
        createdStatement.setUserId(user.getId());
        createdStatement.setStatement(statement);
        createdStatement.setStatementType(statementType);
        createdStatement.setCreateAt(new Date());

        return createdStatement;
    }

    private ApplicationUser findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User is not valid"));
    }
}
