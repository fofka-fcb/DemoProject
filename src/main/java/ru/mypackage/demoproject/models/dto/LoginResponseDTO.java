package ru.mypackage.demoproject.models.dto;

import lombok.Getter;
import lombok.Setter;
import ru.mypackage.demoproject.models.ApplicationUser;

@Getter
@Setter
public class LoginResponseDTO {

    private ApplicationUser user;
    private String jwt;

    public LoginResponseDTO() {
        super();
    }

    public LoginResponseDTO(ApplicationUser user, String jwt) {
        this.user = user;
        this.jwt = jwt;
    }

}
