package ru.mypackage.demoproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserErrorResponse {

    private String message;
    private long timestamp;
}
