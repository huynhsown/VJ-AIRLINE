package com.vietjoke.vn.service.chat;

import com.vietjoke.vn.entity.chat.ChatMessageEntity;
import com.vietjoke.vn.entity.chat.ChatSessionEntity;
import com.vietjoke.vn.entity.user.UserEntity;
import com.vietjoke.vn.util.enums.chat.SenderType;

import java.util.List;

public interface ChatMessageService {
    ChatMessageEntity sendMessage(String sessionId, String content, UserEntity sender, SenderType senderType);
    List<ChatMessageEntity> getSessionMessages(String sessionId);
    void validateMessagePermission(ChatSessionEntity session, UserEntity sender, SenderType senderType);
}
