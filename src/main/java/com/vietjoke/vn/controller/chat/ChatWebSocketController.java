package com.vietjoke.vn.controller.chat;

import com.vietjoke.vn.dto.chat.ChatMessageRequestDTO;
import com.vietjoke.vn.entity.user.UserEntity;
import com.vietjoke.vn.service.chat.ChatWebSocketService;
import com.vietjoke.vn.util.enums.chat.SenderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final ChatWebSocketService chatWebSocketService;

    @MessageMapping("/chat.send")
    public void handleChatMessage(
            @Payload ChatMessageRequestDTO request,
            @AuthenticationPrincipal UserEntity sender) {

        SenderType senderType = determineSenderType(sender);
        chatWebSocketService.sendMessage(
                request.getSessionId(),
                request.getContent(),
                sender,
                senderType
        );
    }

    @SubscribeMapping("/user/queue/chat")
    public void handleUserSubscription(@AuthenticationPrincipal UserEntity user) {
        log.info("User {} đã kết nối WebSocket", user.getUsername());
    }

    private SenderType determineSenderType(UserEntity user) {
        return user.getRoleEntity().getRoleCode().equals("ADMIN")
                ? SenderType.ADMIN
                : SenderType.CUSTOMER;
    }
}
