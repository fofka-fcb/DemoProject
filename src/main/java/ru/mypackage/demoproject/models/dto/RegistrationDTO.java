package ru.mypackage.demoproject.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {

    private String username;
    private String password;

    public RegistrationDTO(){
    }

    public RegistrationDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String toString() {
        return "Registration info: username - " + this.username + " password - " + this.password;
    }
}
