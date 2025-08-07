package com.dinitro.authentication.core.exceptions;

public class LoginAlreadyExistsException extends RuntimeException {
    public LoginAlreadyExistsException(String login) {
        super("Login \'" + login +"\' already exists.");
    }

}
