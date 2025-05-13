package com.vietjoke.vn.dto.chat;

import lombok.Data;

@Data
public class ChatMessageRequestDTO {
    private String sessionId;
    private String content;
}