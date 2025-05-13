package com.vietjoke.vn.service.user.impl;

import com.vietjoke.vn.entity.user.UserEntity;
import com.vietjoke.vn.exception.user.NoAvailableAdminException;
import com.vietjoke.vn.repository.user.UserRepository;
import com.vietjoke.vn.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Set;
import java.util.Comparator;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminStatusService {

    private static final String ADMIN_ONLINE_KEY = "admin:online:";
    private static final String ADMIN_ACTIVE_CHATS_KEY = "admin:active_chats:";
    private static final Duration ONLINE_TIMEOUT = Duration.ofMinutes(5);

    private final RedisTemplate<String, String> redisTemplate;
    private final UserService userService;

    public void markAdminOnline(String adminUsername) {
        UserEntity userEntity = userService.getAdminByUsername(adminUsername);
        String key = ADMIN_ONLINE_KEY + userEntity.getUsername();
        redisTemplate.opsForValue().set(key, "online", ONLINE_TIMEOUT);
    }

    public void markAdminOffline(String adminUsername) {
        UserEntity userEntity = userService.getAdminByUsername(adminUsername);
        String key = ADMIN_ONLINE_KEY + userEntity.getUsername();
        redisTemplate.delete(key);
    }

    public boolean isAdminOnline(String adminUsername) {
        UserEntity userEntity = userService.getAdminByUsername(adminUsername);
        String key = ADMIN_ONLINE_KEY + userEntity.getUsername();
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void incrementActiveChats(String adminUsername) {
        UserEntity userEntity = userService.getAdminByUsername(adminUsername);
        String key = ADMIN_ACTIVE_CHATS_KEY + userEntity.getUsername();
        redisTemplate.opsForValue().increment(key);
    }

    public void decrementActiveChats(String adminUsername) {
        UserEntity userEntity = userService.getAdminByUsername(adminUsername);
        String key = ADMIN_ACTIVE_CHATS_KEY + userEntity.getUsername();
        redisTemplate.opsForValue().decrement(key);
    }

    public int getActiveChatsCount(String adminUsername) {
        UserEntity userEntity = userService.getAdminByUsername(adminUsername);
        String key = ADMIN_ACTIVE_CHATS_KEY + userEntity.getUsername();
        String count = redisTemplate.opsForValue().get(key);
        return count != null ? Integer.parseInt(count) : 0;
    }

    public UserEntity selectAvailableAdmin() {
        Set<String> onlineAdmins = redisTemplate.keys(ADMIN_ONLINE_KEY + "*");

        return onlineAdmins.stream()
                .map(key -> key.replace(ADMIN_ONLINE_KEY, ""))
                .min(Comparator.comparing(this::getActiveChatsCount))
                .map(userService::getAdminByUsername)
                .orElse(null);
    }
}
