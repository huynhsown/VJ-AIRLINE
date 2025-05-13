package com.vietjoke.vn.service.chat.impl;

import com.vietjoke.vn.entity.chat.ChatSessionEntity;
import com.vietjoke.vn.entity.user.UserEntity;
import com.vietjoke.vn.exception.chat.ChatSessionNotFoundException;
import com.vietjoke.vn.exception.user.NoAvailableAdminException;
import com.vietjoke.vn.repository.chat.ChatSessionRepository;
import com.vietjoke.vn.service.chat.ChatSessionService;
import com.vietjoke.vn.service.user.impl.AdminStatusService;
import com.vietjoke.vn.util.enums.chat.ChatSessionStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatSessionServiceImpl implements ChatSessionService {
    private final AdminStatusService adminStatusService;
    private final ChatSessionRepository chatSessionRepository;

    public ChatSessionServiceImpl(AdminStatusService adminStatusService, ChatSessionRepository chatSessionRepository) {
        this.adminStatusService = adminStatusService;
        this.chatSessionRepository = chatSessionRepository;
    }

    @Override
    @Transactional
    public ChatSessionEntity createChatSession(UserEntity customer) {
        UserEntity admin = adminStatusService.selectAvailableAdmin();
        if (admin == null) {
            throw new NoAvailableAdminException("Không có admin nào đang online");
        }
        ChatSessionEntity session = new ChatSessionEntity();
        session.setUser(customer);
        session.setAdmin(admin);
        session.setStatus(ChatSessionStatus.ACTIVE);

        ChatSessionEntity savedSession = chatSessionRepository.save(session);
        adminStatusService.incrementActiveChats(admin.getUsername());
        return savedSession;
    }

    @Override
    public void closeChatSession(String sessionId) {
        ChatSessionEntity session = getSession(sessionId);
        session.setStatus(ChatSessionStatus.CLOSED);
        chatSessionRepository.save(session);
        adminStatusService.decrementActiveChats(session.getAdmin().getUsername());
    }

    @Override
    public ChatSessionEntity getSession(String sessionId) {
        return chatSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ChatSessionNotFoundException(sessionId));
    }
}
