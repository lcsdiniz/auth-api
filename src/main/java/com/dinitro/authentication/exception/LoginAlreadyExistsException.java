package com.dinitro.authentication.exception;

public class LoginAlreadyExistsException extends RuntimeException {
    public LoginAlreadyExistsException(String login) {
        super("Login \'" + login +"\' already exists");
    }

}
