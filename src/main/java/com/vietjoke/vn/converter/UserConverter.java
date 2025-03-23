package com.vietjoke.vn.converter;

import com.vietjoke.vn.dto.user.UserRegisterRequestDTO;
import com.vietjoke.vn.entity.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final ModelMapper modelMapper;

    public UserEntity toUserEntity(UserRegisterRequestDTO user) {
        return modelMapper.map(user, UserEntity.class);
    }
}
