package ru.mypackage.demoproject.exceptions;

public class TypeOfStatementNotValidException extends RuntimeException{

    public TypeOfStatementNotValidException() {
    }

    public TypeOfStatementNotValidException(String message) {
        super(message);
    }
}
