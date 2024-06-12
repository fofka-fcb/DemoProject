package ru.mypackage.demoproject.controllers.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mypackage.demoproject.dto.errors.UserErrorResponse;
import ru.mypackage.demoproject.exceptions.UserNotRegisterException;

import java.util.List;

@RestControllerAdvice
public class ValidationErrorHandlingControllerAdvice {

    @ExceptionHandler(UserNotRegisterException.class)
    public ResponseEntity<UserErrorResponse> handleException(UserNotRegisterException e) {

        StringBuilder exceptionMessage = new StringBuilder();

        List<FieldError> errors = e.getBindingResult().getFieldErrors();

        for (FieldError error : errors) {
            exceptionMessage.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }

        UserErrorResponse response = new UserErrorResponse(
                exceptionMessage.toString(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

}
