package com.vietjoke.vn.service.chat.impl;

import com.vietjoke.vn.entity.chat.ChatMessageEntity;
import com.vietjoke.vn.entity.chat.ChatSessionEntity;
import com.vietjoke.vn.entity.user.UserEntity;
import com.vietjoke.vn.service.chat.ChatMessageService;
import com.vietjoke.vn.service.chat.ChatWebSocketService;
import com.vietjoke.vn.util.enums.chat.SenderType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatWebSocketImpl implements ChatWebSocketService {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendMessage(String sessionId, String content, UserEntity sender, SenderType senderType) {

        ChatMessageEntity message = chatMessageService.sendMessage(sessionId, content, sender, senderType);
        String destination = "/topic/chat/" + sessionId;
        simpMessagingTemplate.convertAndSend(destination, message);
    }

    @Override
    public void notifySessionCreated(ChatSessionEntity session) {
        simpMessagingTemplate.convertAndSendToUser(
                session.getUser().getUsername(),
                "/topic/chat/session",
                session
        );

        simpMessagingTemplate.convertAndSendToUser(
                session.getAdmin().getUsername(),
                "/topic/chat/session",
                session
        );
    }
}
