package ru.mypackage.demoproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mypackage.demoproject.models.Statement;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Integer> {


}
