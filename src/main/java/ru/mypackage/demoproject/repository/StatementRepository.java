package ru.mypackage.demoproject.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mypackage.demoproject.models.Statement;
import ru.mypackage.demoproject.models.StatementType;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Integer> {

    List<Statement> findAllByUsername(String username);
    List<Statement> findAllByUsername(String username, Sort sort);
    List<Statement> findAllByUsername(String username, Pageable pageable);

    Optional<Statement> findByIdAndStatementType(Integer id, StatementType statementType);
    Optional<Statement> findByIdAndUsername(Integer id, String username);

    List<Statement> findAllByStatementTypeAndUsernameStartingWith(StatementType statementType, String username);
    List<Statement> findAllByStatementTypeAndUsernameStartingWith(StatementType statementType, String username, Sort sort);
    List<Statement> findAllByStatementTypeAndUsernameStartingWith(StatementType statementType, String username, Pageable pageable);

    List<Statement> findAllByStatementType(StatementType statementType);
    List<Statement> findAllByStatementType(StatementType statementType, Sort sort);
    List<Statement> findAllByStatementType(StatementType statementType, Pageable pageable);

}
