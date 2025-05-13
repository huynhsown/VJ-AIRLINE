package com.vietjoke.vn.controller.chat;

import com.vietjoke.vn.service.user.impl.AdminStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/chat")
@RequiredArgsConstructor
@Slf4j
public class AdminChatController {

    private final AdminStatusService adminStatusService;

    @PostMapping("/enter")
    public ResponseEntity<Void> adminEnterChat(@AuthenticationPrincipal UserDetails admin) {
        log.info("Admin entered chat");
        adminStatusService.markAdminOnline(admin.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/leave")
    public ResponseEntity<Void> adminLeaveChat(@AuthenticationPrincipal UserDetails admin) {
        adminStatusService.markAdminOffline(admin.getUsername());
        return ResponseEntity.ok().build();
    }
}