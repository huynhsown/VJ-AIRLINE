package com.vietjoke.vn.util.enums.chat;

import lombok.Getter;

@Getter
public enum ChatSessionStatus {
    ACTIVE("Active"),
    WAITING("Waiting for response"),
    CLOSED("Closed"),
    TRANSFERRED("Transferred");

    private final String description;

    ChatSessionStatus(String description) {
        this.description = description;
    }

}
