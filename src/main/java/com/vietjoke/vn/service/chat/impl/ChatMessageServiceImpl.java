package com.vietjoke.vn.service.chat.impl;

import com.vietjoke.vn.entity.chat.ChatMessageEntity;
import com.vietjoke.vn.entity.chat.ChatSessionEntity;
import com.vietjoke.vn.entity.user.UserEntity;
import com.vietjoke.vn.exception.chat.ChatSessionClosedException;
import com.vietjoke.vn.exception.user.UnauthorizedMessageException;
import com.vietjoke.vn.repository.chat.ChatMessageRepository;
import com.vietjoke.vn.service.chat.ChatMessageService;
import com.vietjoke.vn.service.chat.ChatSessionService;
import com.vietjoke.vn.util.enums.chat.ChatSessionStatus;
import com.vietjoke.vn.util.enums.chat.SenderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatSessionService chatSessionService;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessageEntity sendMessage(String sessionId, String content, UserEntity sender, SenderType senderType) {
        ChatSessionEntity session = chatSessionService.getSession(sessionId);

        validateMessagePermission(session, sender, senderType);

        ChatMessageEntity message = new ChatMessageEntity();
        message.setSession(session);
        message.setSenderId(sender.getId());
        message.setSenderType(senderType);
        message.setContent(content);

        return chatMessageRepository.save(message);
    }

    @Override
    public List<ChatMessageEntity> getSessionMessages(String sessionId) {
        return chatMessageRepository.findBySession_SessionIdOrderByCreatedDateAsc(sessionId);
    }

    @Override
    public void validateMessagePermission(ChatSessionEntity session, UserEntity sender, SenderType senderType) {
        if (session.getStatus() != ChatSessionStatus.ACTIVE) {
            throw new ChatSessionClosedException(session.getSessionId());
        }

        if (senderType == SenderType.CUSTOMER && !session.getUser().getId().equals(sender.getId())) {
            throw new UnauthorizedMessageException("User không có quyền gửi tin nhắn trong phiên chat này");
        }

        if (senderType == SenderType.ADMIN && !session.getAdmin().getId().equals(sender.getId())) {
            throw new UnauthorizedMessageException("Admin không có quyền gửi tin nhắn trong phiên chat này");
        }
    }
}
