package ru.mypackage.demoproject.controllers.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mypackage.demoproject.dto.errors.StatementErrorResponse;
import ru.mypackage.demoproject.dto.errors.TokenErrorResponse;
import ru.mypackage.demoproject.dto.errors.UserErrorResponse;
import ru.mypackage.demoproject.exceptions.*;


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
    public ResponseEntity<StatementErrorResponse> handleTypeOfStatementNotValidException(TypeOfStatementNotValidException e) {
        StatementErrorResponse response = new StatementErrorResponse(
                "You can't refactor/delete sent statements!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.LOCKED);
    }

    @ExceptionHandler(StatementSentException.class)
    public ResponseEntity<StatementErrorResponse> handleStatementSentException(StatementSentException e) {
        StatementErrorResponse response = new StatementErrorResponse(
                "This statement has already been sent!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.LOCKED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<TokenErrorResponse> handleTokenExpiredException(TokenExpiredException e) {
        TokenErrorResponse response = new TokenErrorResponse(
                "Token is expired!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}
