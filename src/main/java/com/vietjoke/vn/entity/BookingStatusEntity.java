package com.vietjoke.vn.entity;

import com.vietjoke.vn.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "booking_status")
public class BookingStatusEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status_code", length = 20, nullable = false, unique = true)
    private BookingStatus statusCode;

    @Column(name = "status_name", length = 50, nullable = false)
    private String statusName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
