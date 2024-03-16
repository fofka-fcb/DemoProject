package ru.mypackage.demoproject.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mypackage.demoproject.models.ApplicationUser;
import ru.mypackage.demoproject.models.Statement;
import ru.mypackage.demoproject.models.StatementType;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Integer> {

    Optional<Statement> findByIdAndStatementType(Integer id, StatementType statementType);
    List<Statement> findAllByUserAndStatementType(ApplicationUser user, StatementType statementType);
    List<Statement> findAllByUserAndStatementType(ApplicationUser user, StatementType statementType, Pageable pageable);
    List<Statement> findAllByStatementType(StatementType statementType);

}
