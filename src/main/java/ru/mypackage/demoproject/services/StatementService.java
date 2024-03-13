package ru.mypackage.demoproject.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mypackage.demoproject.models.ApplicationUser;
import ru.mypackage.demoproject.models.Statement;
import ru.mypackage.demoproject.models.StatementType;
import ru.mypackage.demoproject.repository.StatementRepository;
import ru.mypackage.demoproject.repository.UserRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementRepository statementRepository;
    private final UserRepository userRepository;

    public void createSent(String username, String statement) {
        statementRepository.save(createStatement(username, statement, StatementType.SENT));
    }

    public void createDraft(String username, String statement) {
        statementRepository.save(createStatement(username, statement, StatementType.DRAFT));
    }

    private Statement createStatement(String username, String statement, StatementType statementType) {
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User is not valid"));

        Statement createdStatement = new Statement();
        createdStatement.setUserId(user.getId());
        createdStatement.setStatement(statement);
        createdStatement.setStatementType(statementType);
        createdStatement.setCreateAt(new Date());

        return createdStatement;
    }
}
