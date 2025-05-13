package com.vietjoke.vn.service.chat;

import com.vietjoke.vn.entity.chat.ChatSessionEntity;
import com.vietjoke.vn.entity.user.UserEntity;
import com.vietjoke.vn.util.enums.chat.SenderType;

public interface ChatWebSocketService {
    void sendMessage(String sessionId, String content, UserEntity sender, SenderType senderType);
    void notifySessionCreated(ChatSessionEntity session);
}
