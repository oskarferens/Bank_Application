package com.bank.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private String message;

    public ErrorResponse() {
        // IT WAS NEEDED FOR SPRING
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}