package com.vietjoke.vn.controller.chat;

import com.vietjoke.vn.entity.chat.ChatMessageEntity;
import com.vietjoke.vn.entity.chat.ChatSessionEntity;
import com.vietjoke.vn.entity.user.UserEntity;
import com.vietjoke.vn.service.chat.ChatMessageService;
import com.vietjoke.vn.service.chat.ChatSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatSessionService chatSessionService;
    private final ChatMessageService chatMessageService;

    @PostMapping("/sessions")
    public ResponseEntity<ChatSessionEntity> createChatSession(
            @AuthenticationPrincipal UserEntity user) {
        ChatSessionEntity session = chatSessionService.createChatSession(user);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<ChatSessionEntity> getChatSession(
            @PathVariable String sessionId,
            @AuthenticationPrincipal UserEntity user) {
        ChatSessionEntity session = chatSessionService.getSession(sessionId);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/sessions/{sessionId}/close")
    public ResponseEntity<Void> closeChatSession(
            @PathVariable String sessionId,
            @AuthenticationPrincipal UserEntity user) {
        chatSessionService.closeChatSession(sessionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sessions/{sessionId}/messages")
    public ResponseEntity<List<ChatMessageEntity>> getChatMessages(
            @PathVariable String sessionId,
            @AuthenticationPrincipal UserEntity user) {
        List<ChatMessageEntity> messages = chatMessageService.getSessionMessages(sessionId);
        return ResponseEntity.ok(messages);
    }
}