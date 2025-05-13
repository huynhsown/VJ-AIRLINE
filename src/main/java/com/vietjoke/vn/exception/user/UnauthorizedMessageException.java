package com.vietjoke.vn.exception.user;

public class UnauthorizedMessageException extends RuntimeException {
    public UnauthorizedMessageException(String message) {
        super(message);
    }
}
