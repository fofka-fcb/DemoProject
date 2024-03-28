package ru.mypackage.demoproject.models;

import jakarta.persistence.*;
import ru.mypackage.demoproject.models.enums.StatementType;

import java.util.Date;

@Entity
@Table(name = "statements")
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "statement")
    private String statement;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private StatementType statementType;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    public Statement() {
    }

    public Statement(Integer id, String username, String statement, StatementType statementType, Date createAt) {
        this.id = id;
        this.username = username;
        this.statement = statement;
        this.statementType = statementType;
        this.createAt = createAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public StatementType getStatementType() {
        return statementType;
    }

    public void setStatementType(StatementType statementType) {
        this.statementType = statementType;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
