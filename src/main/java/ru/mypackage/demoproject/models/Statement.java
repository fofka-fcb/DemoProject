package ru.mypackage.demoproject.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "statements")
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_of_user")
    @JoinColumn(name = "id_of_user", referencedColumnName = "user_id")
    private Integer userId;

    @Column(name = "statement")
    private String statement;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private StatementType statementType;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

}
