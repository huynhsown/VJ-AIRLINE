package com.vietjoke.vn.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateAvatarRequestDTO {
    @NotNull(message = "Avatar file must not be null")
    private MultipartFile avatar;
}