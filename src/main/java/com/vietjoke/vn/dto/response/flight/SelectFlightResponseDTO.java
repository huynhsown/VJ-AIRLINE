package com.vietjoke.vn.dto.response.flight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectFlightResponseDTO {
    private String sessionToken;
    private LocalDateTime expireAt;
    private Integer tripPassengersAdult;
    private Integer tripPassengersChildren;
    private Integer tripPassengersInfant;
}
