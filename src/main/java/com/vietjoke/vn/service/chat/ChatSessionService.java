package com.vietjoke.vn.service.chat;

import com.vietjoke.vn.entity.chat.ChatSessionEntity;
import com.vietjoke.vn.entity.user.UserEntity;

public interface ChatSessionService {
    ChatSessionEntity createChatSession(UserEntity customer);
    void closeChatSession(String sessionId);
    ChatSessionEntity getSession(String sessionId);
}
