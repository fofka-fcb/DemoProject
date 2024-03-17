package ru.mypackage.demoproject.controllers.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mypackage.demoproject.dto.StatementErrorResponse;
import ru.mypackage.demoproject.dto.UserErrorResponse;
import ru.mypackage.demoproject.exceptions.StatementNotFoundException;
import ru.mypackage.demoproject.exceptions.StatementSentException;
import ru.mypackage.demoproject.exceptions.TypeOfStatementNotValidException;
import ru.mypackage.demoproject.exceptions.UserNotRegisterException;

import java.util.List;

@RestControllerAdvice
public class GlobalErrorHandlingControllerAdvice {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<UserErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        UserErrorResponse response = new UserErrorResponse(
                "Username is not valid!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StatementNotFoundException.class)
    public ResponseEntity<StatementErrorResponse> handleStatementNotFoundException(StatementNotFoundException e) {
        StatementErrorResponse response = new StatementErrorResponse(
                "Statement with this 'id' wasn't found!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TypeOfStatementNotValidException.class)
    ResponseEntity<StatementErrorResponse> handleTypeOfStatementNotValidException(TypeOfStatementNotValidException e) {
        StatementErrorResponse response = new StatementErrorResponse(
                "You can't refactor/delete sent statements!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.LOCKED);
    }

    @ExceptionHandler(StatementSentException.class)
    ResponseEntity<StatementErrorResponse> handleStatementSentException(StatementSentException e) {
        StatementErrorResponse response = new StatementErrorResponse(
                "This statement has already been sent!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.LOCKED);
    }

    @ExceptionHandler(UserNotRegisterException.class)
    private ResponseEntity<UserErrorResponse> handleException(UserNotRegisterException e) {

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

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
