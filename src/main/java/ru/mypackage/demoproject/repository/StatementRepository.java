package ru.mypackage.demoproject.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mypackage.demoproject.models.Statement;
import ru.mypackage.demoproject.models.StatementType;

import java.util.List;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Integer> {

    List<Statement> findAllByUserIdAndStatementType(Integer id, StatementType statementType);
    List<Statement> findAllByUserIdAndStatementType(Integer id, StatementType statementType, Pageable pageable);

}
