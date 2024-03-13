package ru.mypackage.demoproject.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "token")
@Getter
@Setter
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "token")
    private String token;

    @Column(name = "expired")
    private Boolean expired;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TokenType tokenType = TokenType.BEARER;

    @Column(name = "id_of_user")
    @JoinColumn(name = "id_of_user", referencedColumnName = "user_id")
    private Integer userId;
    public Token() {
    }

    public Token(String token, boolean expired, Integer userId) {
        this.token = token;
        this.expired = expired;
        this.userId = userId;
    }
}
