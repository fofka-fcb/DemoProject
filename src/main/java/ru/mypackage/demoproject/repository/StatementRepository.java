package ru.mypackage.demoproject.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mypackage.demoproject.models.ApplicationUser;
import ru.mypackage.demoproject.models.Statement;
import ru.mypackage.demoproject.models.StatementType;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Integer> {

    List<Statement> findAllByUser(ApplicationUser user);
    List<Statement> findAllByUser(ApplicationUser user, Sort sort);
    List<Statement> findAllByUser(ApplicationUser user, Pageable pageable);

//    @Query("SELECT s FROM Statement s WHERE s.statementType = :statementType ORDER BY s.createAt")
//    List<Statement> findAllByStatementTypeAndSortingASC(@Param("statementType") StatementType statementType);
//
//    @Query("SELECT s FROM Statement s WHERE s.statementType = :statementType ORDER BY s.createAt DESC")
//    List<Statement> findAllByStatementTypeAndSortingDESC(@Param("statementType") StatementType statementType);

//    @Query("SELECT s FROM Statement s WHERE s.user = :user ORDER BY s.createAt")
//    List<Statement> findAllByUserAndSortingASC(@Param("user") ApplicationUser user);
//
//    @Query("SELECT s FROM Statement s WHERE s.user = :user ORDER BY s.createAt DESC ")
//    List<Statement> findAllByUserAndSortingDESC(@Param("user") ApplicationUser user);

//    @Query("SELECT s FROM Statement s WHERE s.user = :user AND s.statementType = :statementType ORDER BY s.createAt")
//    List<Statement> findAllByUserAndStatementTypeWithSortingASC(@Param("user")ApplicationUser user,
//                                                                @Param("statementType") StatementType statementType);
//
//    @Query("SELECT s FROM Statement s WHERE s.user = :user AND s.statementType = :statementType ORDER BY s.createAt DESC")
//    List<Statement> findAllByUserAndStatementTypeWithSortingDESC(@Param("user") ApplicationUser user,
//                                                                 @Param("statementType") StatementType statementType);

    Optional<Statement> findByIdAndStatementType(Integer id, StatementType statementType);
    Optional<Statement> findByIdAndUser(Integer id, ApplicationUser user);

    List<Statement> findAllByUserAndStatementType(ApplicationUser user, StatementType statementType);
    List<Statement> findAllByUserAndStatementType(ApplicationUser user, StatementType statementType, Sort sort);
    List<Statement> findAllByUserAndStatementType(ApplicationUser user, StatementType statementType, Pageable pageable);

    List<Statement> findAllByStatementType(StatementType statementType);
    List<Statement> findAllByStatementType(StatementType statementType, Sort sort);
    List<Statement> findAllByStatementType(StatementType statementType, Pageable pageable);

}
