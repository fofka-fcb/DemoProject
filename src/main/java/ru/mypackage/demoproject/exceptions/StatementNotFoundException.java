package ru.mypackage.demoproject.exceptions;

public class StatementNotFoundException extends RuntimeException{

    public StatementNotFoundException() {
    }

    public StatementNotFoundException(String message) {
        super(message);
    }

}
