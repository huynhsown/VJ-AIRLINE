package com.vietjoke.vn.dto.response.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimplifiedPassengerDTO {
    private String uuid;

    private String firstName;

    private String lastName;
}
