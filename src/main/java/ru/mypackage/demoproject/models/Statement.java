package ru.mypackage.demoproject.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "statements")
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_of_user", referencedColumnName = "user_id")
    private ApplicationUser user;

    @Column(name = "statement")
    private String statement;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private StatementType statementType;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser() {
        return user.getId();
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
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
