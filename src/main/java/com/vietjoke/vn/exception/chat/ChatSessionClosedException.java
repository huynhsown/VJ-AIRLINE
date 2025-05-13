package com.vietjoke.vn.exception.chat;

public class ChatSessionClosedException extends RuntimeException {
    public ChatSessionClosedException(String message) {
        super(message);
    }
}
