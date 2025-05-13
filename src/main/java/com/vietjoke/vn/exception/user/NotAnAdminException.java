package com.vietjoke.vn.exception.user;

public class NotAnAdminException extends RuntimeException {
    public NotAnAdminException(String message) {
        super(message);
    }
}
