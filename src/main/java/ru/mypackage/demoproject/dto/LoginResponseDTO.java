package ru.mypackage.demoproject.dto;

import lombok.Getter;
import lombok.Setter;
import ru.mypackage.demoproject.models.Role;

import java.util.Set;

@Getter
@Setter
public class LoginResponseDTO {

    private String username;
    private Set<Role> authorities;
    private String jwt;

    public LoginResponseDTO() {
        super();
    }

    public LoginResponseDTO(String username, Set<Role> authorities, String jwt) {
        this.username = username;
        this.authorities = authorities;
        this.jwt = jwt;
    }
}
