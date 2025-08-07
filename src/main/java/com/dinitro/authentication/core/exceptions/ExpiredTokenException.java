package com.dinitro.authentication.core.exceptions;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException() {
        super("Token is expired.");
    }

}
