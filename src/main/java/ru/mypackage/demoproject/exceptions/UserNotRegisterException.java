package ru.mypackage.demoproject.exceptions;

import org.springframework.validation.BindingResult;

public class UserNotRegisterException extends RuntimeException{

    private BindingResult bindingResult;

    public UserNotRegisterException() {
    }

    public UserNotRegisterException(String message) {
        super(message);
    }

    public UserNotRegisterException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public void setBindingResult(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}

