package com.dinitro.authentication.exception;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException() {
        super("Token is expired.");
    }

}
