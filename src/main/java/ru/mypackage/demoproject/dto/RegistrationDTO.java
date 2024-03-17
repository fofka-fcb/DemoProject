package ru.mypackage.demoproject.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 4, max = 12, message = "Password should be between 4 and 12 characters")
    private String password;

    @NotEmpty(message = "Phone should not be empty")
    private String phone;

}
