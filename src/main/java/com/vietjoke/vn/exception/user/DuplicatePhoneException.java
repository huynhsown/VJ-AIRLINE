package com.vietjoke.vn.exception.user;

public class DuplicatePhoneException extends RuntimeException {
    public DuplicatePhoneException(String message) {
        super(message);
    }
}
