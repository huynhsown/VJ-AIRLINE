package com.vietjoke.vn.config.seeding.jsonObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Route {
    private Integer distance;           // Khoảng cách (km)
    private Integer estimatedDuration;  // Thời gian ước tính (phút)
    private boolean isActive;           // Trạng thái hoạt động
    private String originAirportEntity; // Mã sân bay xuất phát
    private String destinationAirportEntity; // Mã sân bay đích
}