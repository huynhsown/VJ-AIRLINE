package com.vietjoke.vn.entity.chat;

import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.entity.user.UserEntity;
import com.vietjoke.vn.util.enums.chat.ChatSessionStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_sessions")
@Getter
@Setter
public class ChatSessionEntity extends BaseEntity {
    @Column(name = "session", nullable = false, unique = true)
    private String sessionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private UserEntity admin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatSessionStatus status;

    @PrePersist
    protected void onCreate() {
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
        }
    }
}