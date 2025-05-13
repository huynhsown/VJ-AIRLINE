package com.vietjoke.vn.repository.chat;

import com.vietjoke.vn.entity.chat.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findBySession_SessionIdOrderByCreatedDateAsc(String sessionId);
}
