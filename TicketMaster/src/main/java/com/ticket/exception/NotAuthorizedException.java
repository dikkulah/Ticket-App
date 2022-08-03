package com.ticket.exception;

public class NotAuthorizedException extends RuntimeException {
    private static final String NOT_AUTHORIZED = "Bu işlemi gerekleştirmek için yetkiniz yok.";
    public NotAuthorizedException() {
        super(NOT_AUTHORIZED);
    }
}
