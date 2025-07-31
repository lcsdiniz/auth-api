package com.dinitro.authentication.auth;

public enum TokenType {
    REFRESH("refresh"),
    ACCESS("access");

    private String type;

    TokenType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
