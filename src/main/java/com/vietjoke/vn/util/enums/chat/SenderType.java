package com.vietjoke.vn.util.enums.chat;

import lombok.Getter;

@Getter
public enum SenderType {
    CUSTOMER("Customer"),
    ADMIN("Admin"),
    SYSTEM("System");

    private final String description;

    SenderType(String description) {
        this.description = description;
    }
}
