package ru.mypackage.demoproject.exceptions;

public class StatementSentException extends RuntimeException{

    public StatementSentException() {
    }

    public StatementSentException(String message) {
        super(message);
    }

}
