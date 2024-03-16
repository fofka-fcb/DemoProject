package ru.mypackage.demoproject.models;


import jakarta.persistence.*;

@Entity
@Table(name = "token")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_of_user", referencedColumnName = "user_id")
    private ApplicationUser user;

    public Token() {
    }

    public Token(String token, boolean expired, ApplicationUser user) {
        this.token = token;
        this.expired = expired;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getUser() {
        return user.getId();
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }
}
